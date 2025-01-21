package com.github.cedricrev.skriptbedrock.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.github.cedricrev.skriptbedrock.SkriptBedrock;
import com.github.cedricrev.skriptbedrock.elements.events.FormCloseEvent;
import com.github.cedricrev.skriptbedrock.elements.events.FormEventHandler;
import com.github.cedricrev.skriptbedrock.elements.events.FormOpenEvent;
import com.github.cedricrev.skriptbedrock.elements.events.FormSubmitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.geysermc.cumulus.component.util.ComponentType;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.form.util.FormBuilder;
import org.geysermc.cumulus.form.util.FormType;
import org.geysermc.cumulus.response.FormResponse;
import org.geysermc.cumulus.response.ModalFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

public class Form {
    private final FormEventHandler eventHandler = new FormEventHandler(){

        @Override
        public void onSubmit(FormSubmitEvent event) {
            Consumer run;
            if (this.isPaused() || this.isPaused(event.getPlayer())) {
                return;
            }
            FormResponse response = event.getResponse();
            switch (event.getForm().getType()) {
                case SIMPLE_FORM: {
                    run = Form.this.buttons.get(((SimpleFormResponse)response).clickedButtonId());
                    break;
                }
                case MODAL_FORM: {
                    run = Form.this.buttons.get(((ModalFormResponse)response).clickedButtonId());
                    break;
                }
                default: {
                    run = Form.this.onResult;
                }
            }
            if (run != null) {
                FormManager.getFormManager().setForm(event, Form.this);
                run.accept(event);
            }
        }

        @Override
        public void onOpen(FormOpenEvent event) {
            if (this.isPaused() || this.isPaused(event.getPlayer())) {
                return;
            }
            if (Form.this.onOpen != null) {
                FormManager.getFormManager().setForm(event, Form.this);
                Form.this.onOpen.accept(event);
            }
        }

        @Override
        public void onClose(FormCloseEvent event) {
            if (this.isPaused() || this.isPaused(event.getPlayer())) {
                return;
            }
            if (Form.this.onClose != null) {
                FormManager.getFormManager().setForm(event, Form.this);
                Form.this.onClose.accept(event);
                if (Form.this.closeCancelled) {
                    Bukkit.getScheduler().runTaskLater(SkriptBedrock.PLUGIN.getPlugin(), () -> {
                        Form.this.setCloseCancelled(false);
                        Player closer = event.getPlayer();
                        this.pause(closer);
                        FloodgateApi.getInstance().getPlayer(closer.getUniqueId()).sendForm(Form.this.form);
                        this.resume(closer);
                    }, 1L);
                    return;
                }
            }
        }
    };
    FormBuilder form;
    FormType type;
    private String title;
    private boolean closeCancelled;
    private ArrayList<ComponentType> components;
    private Consumer<FormOpenEvent> onOpen;
    private Consumer<FormCloseEvent> onClose;
    private Consumer<FormSubmitEvent> onResult;
    private final Map<Integer, Consumer<FormSubmitEvent>> buttons = new HashMap<Integer, Consumer<FormSubmitEvent>>();
    int last_button = 0;

    public Form(FormType type, String title) {
        this.title = title;
        switch (type) {
            case MODAL_FORM: {
                this.form = ModalForm.builder().title(title);
                break;
            }
            case SIMPLE_FORM: {
                this.form = SimpleForm.builder().title(title);
                break;
            }
            default: {
                this.form = CustomForm.builder().title(title);
                this.components = new ArrayList();
            }
        }
        this.type = type;
    }

    public void setOnOpen(Consumer<FormOpenEvent> onOpen) {
        this.onOpen = onOpen;
    }

    public void setOnClose(Consumer<FormCloseEvent> onClose) {
        this.onClose = onClose;
    }

    public void setOnResult(Consumer<FormSubmitEvent> onResult) {
        this.onResult = onResult;
    }

    public FormEventHandler getEventHandler() {
        return this.eventHandler;
    }

    public FormBuilder getForm() {
        return this.form;
    }

    public FormType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCloseCancelled(boolean cancel) {
        this.closeCancelled = cancel;
    }

    public int getLastButton() {
        return this.last_button;
    }

    public void setButton(Consumer<FormSubmitEvent> con) {
        this.buttons.put(this.last_button, con);
        ++this.last_button;
    }

    public void addComponent(ComponentType type) {
        this.components.add(type);
    }

    public ArrayList<ComponentType> getComponents() {
        return this.components;
    }

    public org.geysermc.cumulus.form.Form build(Player player) {
        Bukkit.getPluginManager().callEvent((Event)new FormOpenEvent(player, this));
        switch (this.type) {
            case MODAL_FORM: {
                ((ModalForm.Builder)this.getForm()).closedOrInvalidResultHandler((currentForm, response) -> {
                    if (response.isInvalid()) {
                        Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.INVALID_RESPONSE));
                    } else {
                        Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.CLOSE));
                    }
                });
                ((ModalForm.Builder)this.getForm()).validResultHandler((currentForm, response) -> {
                    Bukkit.getPluginManager().callEvent((Event)new FormSubmitEvent(player, this, (FormResponse)response));
                    Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.SUBMIT));
                });
                break;
            }
            case SIMPLE_FORM: {
                ((SimpleForm.Builder)this.getForm()).closedOrInvalidResultHandler((currentForm, response) -> {
                    if (response.isInvalid()) {
                        Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.INVALID_RESPONSE));
                    } else {
                        Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.CLOSE));
                    }
                });
                ((SimpleForm.Builder)this.getForm()).validResultHandler((currentForm, response) -> {
                    Bukkit.getPluginManager().callEvent((Event)new FormSubmitEvent(player, this, (FormResponse)response));
                    Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.SUBMIT));
                });
                break;
            }
            default: {
                ((CustomForm.Builder)this.getForm()).closedOrInvalidResultHandler((currentForm, response) -> {
                    if (response.isInvalid()) {
                        Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.INVALID_RESPONSE));
                    } else {
                        Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.CLOSE));
                    }
                });
                ((CustomForm.Builder)this.getForm()).validResultHandler((currentForm, response) -> {
                    Bukkit.getPluginManager().callEvent((Event)new FormSubmitEvent(player, this, (FormResponse)response));
                    Bukkit.getPluginManager().callEvent((Event)new FormCloseEvent(player, this, FormCloseEvent.CloseReason.SUBMIT));
                });
            }
        }
        return this.getForm().build();
    }
}

