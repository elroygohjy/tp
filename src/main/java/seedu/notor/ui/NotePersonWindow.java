package seedu.notor.ui;

import javafx.fxml.FXML;
import seedu.notor.logic.Logic;
import seedu.notor.logic.commands.exceptions.CommandException;
import seedu.notor.model.common.Note;
import seedu.notor.model.person.Person;

public class NotePersonWindow extends NoteWindow {

    private static final String MESSAGE_SAVE_NOTE_SUCCESS = "Saved Note to Person: %1$s";
    private static final String MESSAGE_EXIT_NOTE_SUCCESS = "Exited Note of Person: %1$s";

    private Person person;

    /**
     * Creates a new NoteWindow.
     */
    public NotePersonWindow(Person person, Logic logic, ResultDisplay resultDisplay) {
        super(logic, resultDisplay);
        noteTextArea.setText(person.getNote().value);
        this.person = person;
        confirmationWindow = new ConfirmationWindow(person.getName().toString(), this);
        getRoot().setTitle(person.getName().toString());
        noteTextArea.setWrapText(true);
        getRoot().setOnCloseRequest(e -> {
            e.consume();
            handleExit();
        });
    }

    /**
     * Generates a command execution success message based on whether
     * the note is added.
     * {@code personToEdit}.
     */
    @Override
    public String generateSuccessMessage(String message) {
        return String.format(message, person);
    }


    /**
     * Saves the file
     */
    @FXML
    @Override
    public void handleSave() throws CommandException {
        String paragraph = noteTextArea.getText();
        Person editedPerson;
        if (!paragraph.isEmpty()) {
            Note editedNote = new Note(paragraph, noteLastModified());
            editedPerson = new Person(person.getName(), person.getPhone(), person.getEmail(),
                    editedNote, person.getTags());
        } else {
            editedPerson = new Person(person.getName(), person.getPhone(), person.getEmail(),
                    Note.EMPTY_NOTE, person.getTags());
        }
        logic.executeSaveNote(person, editedPerson);
        resultDisplay.setFeedbackToUser(generateSuccessMessage(MESSAGE_SAVE_NOTE_SUCCESS));
    }


    /**
     * Checks if current Note is saved.
     */
    @Override
    public boolean isSave() {
        return person.getNote().value.equals(noteTextArea.getText());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NotePersonWindow)) {
            return false;
        }

        NotePersonWindow otherPerson = (NotePersonWindow) other;
        return otherPerson.person.equals(this.person);

    }

    /**
     * Exits the note Window.
     */
    @Override
    public void exit() {
        getRoot().close();
        OPENED_NOTE_WINDOWS.remove(this);
        resultDisplay.setFeedbackToUser(generateSuccessMessage(MESSAGE_EXIT_NOTE_SUCCESS));
    }
}
