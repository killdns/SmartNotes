package xyz.danshin.smartnotes.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import xyz.danshin.smartnotes.entity.Note;

/**
 * Класс-репозиторий для записок
 */
public class NoteRepository {
    private static final ArrayList<Note> notesList = new ArrayList<>();

    /**
     * Получает коллекцию экземпляров
     * @return Коллекция экземпляров
     */
    public static ArrayList<Note> getCollection() {return notesList;}
    /**
     * Получает размер коллекции
     * @return Размер коллекции
     */
    public static int size()
    {
        return notesList.size();
    }

    /**
     * Получает экзмпляр заметки по ее индексу
     * @param index Индекс заметки
     * @return Экземпляр заметки
     */
    public static Note get(int index)
    {
        return notesList.get(index);
    }

    /**
     * Получает экзмпляр заметки по ее Id
     * @param id Id заметки
     * @return Экземпляр заметки
     */
    public static Note getNoteById(int id)
    {
        for (Note note : notesList)
        {
            if (note.getId() == id)
                return note;
        }
        return null;
    }

    /**
     * Получение всех выделенных заметок
     * @return Списиок с выделенными заметками
     */
    public static ArrayList<Note> getSelectedNotes() {
        ArrayList<Note> selectedItems = new ArrayList<>();
        for (Note item : notesList) {
            if (item.isSelected())
                selectedItems.add(item);
        }
        return selectedItems;
    }

    /**
     * Добавляет экзмпляр заметки в коллекцию
     * @param note Экзмпляр заметки
     */
    public static void add(Note note)
    {
        notesList.add(0, note);
    }

    /**
     * Удаелет экзмпляр заметки из коллекции
     * @param note Экзмпляр заметки
     */
    public static void remove(Note note)
    {
        notesList.remove(note);
    }

    /**
     * Удаелет экзмпляр заметки из коллекции по ее Id
     * @param id Id заметки
     */
    public static void removeById(int id)
    {
        for (Note note : notesList)
        {
            if (note.getId() == id) {
                notesList.remove(note);
                return;
            }
        }
    }

    /**
     * Меняет индекс экземпляра в списке на самый верхний
     * @param note Экзмпляр заметки
     * @throws Exception Ошибка нахождения экземпляра в коллекции
     */
    public static void relocate(Note note) throws Exception
    {
        if (notesList.indexOf(note) < 0)
            throw new Exception("Note not included in the collection");

        remove(note);
        add(note);
    }
    /**
     * Удаление всех выделенных заметок
     */
    public static void removeSelectedNotes()
    {
        for (Note note: getSelectedNotes()) {
            if (note.isSelected())
                notesList.remove(note);
        }
    }
    /**
     * Очистка выделения
     */
    public static void clearSelection()
    {
        for (Note note: notesList) {
            note.setSelected(false);
        }
    }

    /**
     * Получает следующее свободное значение Id
     * @return Свободное значение Id
     */
    public static int getNextId()
    {
        int max = 0;
        for (Note note : notesList)
        {
            if (note.getId() > max)
                max = note.getId();
        }
        return max + 1;
    }

    /**
     * Генерация тестовых заметок
     */
    public static void addTestNotes(){
        if(notesList!=null) notesList.clear();
        for (int i = 1; i <= 50; i++) {
            Date dt = new Date();
            dt.setTime(dt.getTime() -  1200 * 1000 + i*1000);
            notesList.add(new Note("Title " + i, "Description " + i, dt));
        }
        Collections.sort(notesList);
    }
}
