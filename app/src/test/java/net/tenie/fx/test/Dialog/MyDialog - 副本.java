package net.tenie.fx.test.Dialog;


import java.lang.ref.WeakReference;
import java.util.Optional;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback; 

public class MyDialog<R> implements EventTarget {


    FXDialog dialog  ;

    private boolean isClosing;

    public MyDialog() {
        this.dialog = new HeavyweightDialog(this);
        setDialogPane(new MyDialogPane());
        initModality(Modality.APPLICATION_MODAL);
    }


    public final void show() {

        Event.fireEvent(this, new MyDialogEvent(this, MyDialogEvent.DIALOG_SHOWING));
        if (Double.isNaN(getWidth()) && Double.isNaN(getHeight())) {
            dialog.sizeToScene();
        }

        dialog.show();

        Event.fireEvent(this, new MyDialogEvent(this, MyDialogEvent.DIALOG_SHOWN));
    }

    /**
     * Shows the dialog and waits for the user response (in other words, brings
     * up a blocking dialog, with the returned value the users input).
     * <p>
     * This method must be called on the JavaFX Application thread.
     * Additionally, it must either be called from an input event handler or
     * from the run method of a Runnable passed to
     * {@link javafx.application.Platform#runLater Platform.runLater}.
     * It must not be called during animation or layout processing.
     * </p>
     *
     * @return An {@link Optional} that contains the {@link #resultProperty() result}.
     *         Refer to the {@link Dialog} class documentation for more detail.
     * @throws IllegalStateException if this method is called on a thread
     *     other than the JavaFX Application Thread.
     * @throws IllegalStateException if this method is called during
     *     animation or layout processing.
     */
    public final Optional<R> showAndWait() {

       
        Event.fireEvent(this, new MyDialogEvent(this, MyDialogEvent.DIALOG_SHOWING));
        if (Double.isNaN(getWidth()) && Double.isNaN(getHeight())) {
            dialog.sizeToScene();
        }


        // this is slightly odd - we fire the SHOWN event before the show()
        // call, so that users get the event before the dialog blocks
        Event.fireEvent(this, new MyDialogEvent(this, MyDialogEvent.DIALOG_SHOWN));

        dialog.showAndWait();

        return Optional.ofNullable(getResult());
    }

    /**
     * Closes this {@code Dialog}.
     * This call is equivalent to {@link #hide}.
     */
    public final void close() {
        if (isClosing) return;
        isClosing = true;

        final R result = getResult();

        // if the result is null and we do not have permission to close the
        // dialog, then we cancel the close request before any events are
        // even fired
        if (result == null && ! dialog.requestPermissionToClose(this)) {
            isClosing = false;
            return;
        }

        if (result == null) {
            ButtonType cancelButton = null;

            setResultAndClose(cancelButton, false);
        }

        // start normal closing process
        Event.fireEvent(this, new MyDialogEvent(this, MyDialogEvent.DIALOG_HIDING));

        MyDialogEvent closeRequestEvent = new MyDialogEvent(this, MyDialogEvent.DIALOG_CLOSE_REQUEST);
        Event.fireEvent(this, closeRequestEvent);
        if (closeRequestEvent.isConsumed()) {
            isClosing = false;
            return;
        }

        dialog.close();

        Event.fireEvent(this, new MyDialogEvent(this, MyDialogEvent.DIALOG_HIDDEN));

        isClosing = false;
    }

    /**
     * Hides this {@code Dialog}.
     */
    public final void hide() {
        close();
    }

    /**
     * Specifies the modality for this dialog. This must be done prior to making
     * the dialog visible. The modality is one of: Modality.NONE,
     * Modality.WINDOW_MODAL, or Modality.APPLICATION_MODAL.
     *
     * @param modality the modality for this dialog.
     *
     * @throws IllegalStateException if this property is set after the dialog
     * has ever been made visible.
     *
     * @defaultValue Modality.APPLICATION_MODAL
     */
    public final void initModality(Modality modality) {
        dialog.initModality(modality);
    }

    /**
     * Retrieves the modality attribute for this dialog.
     *
     * @return the modality.
     */
    public final Modality getModality() {
        return dialog.getModality();
    }

    /**
     * Specifies the style for this dialog. This must be done prior to making
     * the dialog visible. The style is one of: StageStyle.DECORATED,
     * StageStyle.UNDECORATED, StageStyle.TRANSPARENT, StageStyle.UTILITY,
     * or StageStyle.UNIFIED.
     *
     * @param style the style for this dialog.
     *
     * @throws IllegalStateException if this property is set after the dialog
     * has ever been made visible.
     *
     * @defaultValue StageStyle.DECORATED
     */
    public final void initStyle(StageStyle style) {
        dialog.initStyle(style);
    }

    /**
     * Specifies the owner {@link Window} for this dialog, or null for a top-level,
     * unowned dialog. This must be done prior to making the dialog visible.
     *
     * @param window the owner {@link Window} for this dialog.
     *
     * @throws IllegalStateException if this property is set after the dialog
     * has ever been made visible.
     *
     * @defaultValue null
     */
    public final void initOwner(Window window) {
        dialog.initOwner(window);
    }

    /**
     * Retrieves the owner Window for this dialog, or null for an unowned dialog.
     *
     * @return the owner Window.
     */
    public final Window getOwner() {
        return dialog.getOwner();
    }



    /* ************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    // --- dialog Pane
    /**
     * The root node of the dialog, the {@link DialogPane} contains all visual
     * elements shown in the dialog. As such, it is possible to completely adjust
     * the display of the dialog by modifying the existing dialog pane or creating
     * a new one.
     */
    private ObjectProperty<MyDialogPane> dialogPane = new SimpleObjectProperty<MyDialogPane>(this, "dialogPane", new MyDialogPane()) {
        final InvalidationListener expandedListener = o -> {
        	MyDialogPane dialogPane = getDialogPane();
            if (dialogPane == null) return;

            final Node content = dialogPane.getExpandableContent();
            final boolean isExpanded = content == null ? false : content.isVisible();
            setResizable(isExpanded);

            MyDialog.this.dialog.sizeToScene();
        };


        WeakReference<MyDialogPane> dialogPaneRef = new WeakReference<>(null);

        @Override
        protected void invalidated() {
            MyDialogPane oldDialogPane = dialogPaneRef.get();
            if (oldDialogPane != null) {
            }

            final MyDialogPane newDialogPane = getDialogPane();


            // push the new dialog down into the implementation for rendering
            dialog.setDialogPane(newDialogPane);

            dialogPaneRef = new WeakReference<MyDialogPane>(newDialogPane);
        }
    };

    public final ObjectProperty<MyDialogPane> dialogPaneProperty() {
        return dialogPane;
    }

    public final MyDialogPane getDialogPane() {
        return dialogPane.get();
    }

    public final void setDialogPane(MyDialogPane value) {
        dialogPane.set(value);
    }


    public final StringProperty contentTextProperty() {
        return getDialogPane().contentTextProperty();
    }
    public final String getContentText() {
        return getDialogPane().getContentText();
    }

    public final void setContentText(String contentText) {
        getDialogPane().setContentText(contentText);
    }



    // --- result
    private final ObjectProperty<R> resultProperty = new SimpleObjectProperty<R>() {
        protected void invalidated() {
            close();
        }
    };

    /**
     * A property representing what has been returned from the dialog. A result
     * is generated through the {@link #resultConverterProperty() result converter},
     * which is intended to convert from the {@link ButtonType} that the user
     * clicked on into a value of type R. Refer to the {@link Dialog} class
     * JavaDoc for more details.
     * @return a property representing what has been returned from the dialog
     */
    public final ObjectProperty<R> resultProperty() {
        return resultProperty;
    }

    public final R getResult() {
        return resultProperty().get();
    }

    public final void setResult(R value) {
        this.resultProperty().set(value);
    }


    // --- result converter
    private final ObjectProperty<Callback<ButtonType, R>> resultConverterProperty
        = new SimpleObjectProperty<>(this, "resultConverter");

    /**
     * API to convert the {@link ButtonType} that the user clicked on into a
     * result that can be returned via the {@link #resultProperty() result}
     * property. This is necessary as {@link ButtonType} represents the visual
     * button within the dialog, and do not know how to map themselves to a valid
     * result - that is a requirement of the dialog implementation by making use
     * of the result converter. In some cases, the result type of a Dialog
     * subclass is ButtonType (which means that the result converter can be null),
     * but in some cases (where the result type, R, is not ButtonType or Void),
     * this callback must be specified.
     * @return the API to convert the {@link ButtonType} that the user clicked on
     */
    public final ObjectProperty<Callback<ButtonType, R>> resultConverterProperty() {
        return resultConverterProperty;
    }

    public final Callback<ButtonType, R> getResultConverter() {
        return resultConverterProperty().get();
    }

    public final void setResultConverter(Callback<ButtonType, R> value) {
        this.resultConverterProperty().set(value);
    }


    // --- showing
    /**
     * Represents whether the dialog is currently showing.
     * @return the property representing whether the dialog is currently showing
     */
    public final ReadOnlyBooleanProperty showingProperty() {
        return dialog.showingProperty();
    }

    /**
     * Returns whether or not the dialog is showing.
     *
     * @return true if dialog is showing.
     */
    public final boolean isShowing() {
        return showingProperty().get();
    }


    // --- resizable
    /**
     * Represents whether the dialog is resizable.
     * @return the property representing whether the dialog is resizable
     */
    public final BooleanProperty resizableProperty() {
        return dialog.resizableProperty();
    }

    /**
     * Returns whether or not the dialog is resizable.
     *
     * @return true if dialog is resizable.
     */
    public final boolean isResizable() {
        return resizableProperty().get();
    }

    /**
     * Sets whether the dialog can be resized by the user.
     * Resizable dialogs can also be maximized ( maximize button
     * becomes visible)
     *
     * @param resizable true if dialog should be resizable.
     */
    public final void setResizable(boolean resizable) {
        resizableProperty().set(resizable);
    }


    // --- width
    /**
     * Property representing the width of the dialog.
     * @return the property representing the width of the dialog
     */
    public final ReadOnlyDoubleProperty widthProperty() {
        return dialog.widthProperty();
    }

    /**
     * Returns the width of the dialog.
     * @return the width of the dialog
     */
    public final double getWidth() {
        return widthProperty().get();
    }

    /**
     * Sets the width of the dialog.
     * @param width the width of the dialog
     */
    public final void setWidth(double width) {
        dialog.setWidth(width);
    }


    // --- height
    /**
     * Property representing the height of the dialog.
     * @return the property representing the height of the dialog
     */
    public final ReadOnlyDoubleProperty heightProperty() {
        return dialog.heightProperty();
    }

    /**
     * Returns the height of the dialog.
     * @return the height of the dialog
     */
    public final double getHeight() {
        return heightProperty().get();
    }

    /**
     * Sets the height of the dialog.
     * @param height the height of the dialog
     */
    public final void setHeight(double height) {
        dialog.setHeight(height);
    }


    // --- title
    /**
     * Return the titleProperty of the dialog.
     * @return the titleProperty of the dialog
     */
    public final StringProperty titleProperty(){
        return this.dialog.titleProperty();
    }

    /**
     * Return the title of the dialog.
     * @return the title of the dialog
     */
    public final String getTitle(){
        return this.dialog.titleProperty().get();
    }
    /**
     * Change the Title of the dialog.
     * @param title the Title of the dialog
     */
    public final void setTitle(String title){
        this.dialog.titleProperty().set(title);
    }


    // --- x
    public final double getX() {
        return dialog.getX();
    }

    public final void setX(double x) {
        dialog.setX(x);
    }

    /**
     * The horizontal location of this {@code Dialog}. Changing this attribute
     * will move the {@code Dialog} horizontally.
     * @return the horizontal location of this {@code Dialog}
     */
    public final ReadOnlyDoubleProperty xProperty() {
        return dialog.xProperty();
    }

    // --- y
    public final double getY() {
        return dialog.getY();
    }

    public final void setY(double y) {
        dialog.setY(y);
    }

    /**
     * The vertical location of this {@code Dialog}. Changing this attribute
     * will move the {@code Dialog} vertically.
     * @return the vertical location of this {@code Dialog}
     */
    public final ReadOnlyDoubleProperty yProperty() {
        return dialog.yProperty();
    }



    /* *************************************************************************
     *
     * Events
     *
     **************************************************************************/

    private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);

    /** {@inheritDoc} */
    @Override public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(eventHandlerManager);
    }

    /**
     * Called just prior to the Dialog being shown.
     */
    private ObjectProperty<EventHandler<MyDialogEvent>> onShowing;
    public final void setOnShowing(EventHandler<MyDialogEvent> value) { onShowingProperty().set(value); }
    public final EventHandler<MyDialogEvent> getOnShowing() {
        return onShowing == null ? null : onShowing.get();
    }
    public final ObjectProperty<EventHandler<MyDialogEvent>> onShowingProperty() {
        if (onShowing == null) {
            onShowing = new SimpleObjectProperty<EventHandler<MyDialogEvent>>(this, "onShowing") {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(MyDialogEvent.DIALOG_SHOWING, get());
                }
            };
        }
        return onShowing;
    }

    /**
     * Called just after the Dialog is shown.
     */
    private ObjectProperty<EventHandler<MyDialogEvent>> onShown;
    public final void setOnShown(EventHandler<MyDialogEvent> value) { onShownProperty().set(value); }
    public final EventHandler<MyDialogEvent> getOnShown() {
        return onShown == null ? null : onShown.get();
    }
    public final ObjectProperty<EventHandler<MyDialogEvent>> onShownProperty() {
        if (onShown == null) {
            onShown = new SimpleObjectProperty<EventHandler<MyDialogEvent>>(this, "onShown") {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(MyDialogEvent.DIALOG_SHOWN, get());
                }
            };
        }
        return onShown;
    }

    /**
     * Called just prior to the Dialog being hidden.
     */
    private ObjectProperty<EventHandler<MyDialogEvent>> onHiding;
    public final void setOnHiding(EventHandler<MyDialogEvent> value) { onHidingProperty().set(value); }
    public final EventHandler<MyDialogEvent> getOnHiding() {
        return onHiding == null ? null : onHiding.get();
    }
    public final ObjectProperty<EventHandler<MyDialogEvent>> onHidingProperty() {
        if (onHiding == null) {
            onHiding = new SimpleObjectProperty<EventHandler<MyDialogEvent>>(this, "onHiding") {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(MyDialogEvent.DIALOG_HIDING, get());
                }
            };
        }
        return onHiding;
    }

    /**
     * Called just after the Dialog has been hidden.
     * When the {@code Dialog} is hidden, this event handler is invoked allowing
     * the developer to clean up resources or perform other tasks when the
     * {@link Alert} is closed.
     */
    private ObjectProperty<EventHandler<MyDialogEvent>> onHidden;
    public final void setOnHidden(EventHandler<MyDialogEvent> value) { onHiddenProperty().set(value); }
    public final EventHandler<MyDialogEvent> getOnHidden() {
        return onHidden == null ? null : onHidden.get();
    }
    public final ObjectProperty<EventHandler<MyDialogEvent>> onHiddenProperty() {
        if (onHidden == null) {
            onHidden = new SimpleObjectProperty<EventHandler<MyDialogEvent>>(this, "onHidden") {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(MyDialogEvent.DIALOG_HIDDEN, get());
                }
            };
        }
        return onHidden;
    }

    /**
     * Called when there is an external request to close this {@code Dialog}.
     * The installed event handler can prevent dialog closing by consuming the
     * received event.
     */
    private ObjectProperty<EventHandler<MyDialogEvent>> onCloseRequest;
    public final void setOnCloseRequest(EventHandler<MyDialogEvent> value) {
        onCloseRequestProperty().set(value);
    }
    public final EventHandler<MyDialogEvent> getOnCloseRequest() {
        return (onCloseRequest != null) ? onCloseRequest.get() : null;
    }
    public final ObjectProperty<EventHandler<MyDialogEvent>>
            onCloseRequestProperty() {
        if (onCloseRequest == null) {
            onCloseRequest = new SimpleObjectProperty<EventHandler<MyDialogEvent>>(this, "onCloseRequest") {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(MyDialogEvent.DIALOG_CLOSE_REQUEST, get());
                }
            };
        }
        return onCloseRequest;
    }



    /* *************************************************************************
     *
     * Private implementation
     *
     **************************************************************************/

    // This code is called both in the normal and in the abnormal case (i.e.
    // both when a button is clicked and when the user forces a window closed
    // with keyboard OS-specific shortcuts or OS-native titlebar buttons).
    @SuppressWarnings("unchecked")
    void setResultAndClose(ButtonType cmd, boolean close) {
        Callback<ButtonType, R> resultConverter = getResultConverter();

        R priorResultValue = getResult();
        R newResultValue = null;

        if (resultConverter == null) {
            // The choice to cast cmd to R here was a conscious decision, taking
            // into account the choices available to us. Firstly, to summarise the
            // issue, at this point here we have a null result converter, and no
            // idea how to convert the given ButtonType to R. Our options are:
            //
            // 1) We could throw an exception here, but this requires that all
            // developers who create a dialog set a result converter (at least
            // setResultConverter(buttonType -> (R) buttonType)). This is
            // non-intuitive and depends on the developer reading documentation.
            //
            // 2) We could set a default result converter in the resultConverter
            // property that does the identity conversion. This saves people from
            // having to set a default result converter, but it is a little odd
            // that the result converter is non-null by default.
            //
            // 3) We can cast the button type here, which is what we do. This means
            // that the result converter is null by default.
            //
            // In the case of option 1), developers will receive a NPE when the
            // dialog is closed, regardless of how it was closed. In the case of
            // option 2) and 3), the user unfortunately receives a ClassCastException
            // in their code. This is unfortunate as it is not immediately obvious
            // why the ClassCastException occurred, and how to resolve it. However,
            // we decided to take this later approach as it prevents the issue of
            // requiring all custom dialog developers from having to supply their
            // own result converters.
            newResultValue = (R) cmd;
        } else {
            newResultValue = resultConverter.call(cmd);
        }

        setResult(newResultValue);

        // fix for the case where we set the same result as what
        // was already set. We should still close the dialog, but
        // we need to special-case it here, as the result property
        // won't fire any event if the value won't change.
        if (close && priorResultValue == newResultValue) {
            close();
        }
    }




    /* *************************************************************************
     *
     * Stylesheet Handling
     *
     **************************************************************************/
    private static final PseudoClass HEADER_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("header"); //$NON-NLS-1$
    private static final PseudoClass NO_HEADER_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("no-header"); //$NON-NLS-1$

}
