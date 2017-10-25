package xyz.danshin.smartnotes.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.entity.Note;

/**
 * Класс контроллер для экспорта заметок
 */
public final class FileController {

    /**
     * Сохранение заметок
     * @param notes Коллекция заметок
     * @param path Родительский пусть
     * @param context Context
     */
    public static void exportNotes(ArrayList<Note> notes, Uri path, Context context)
    {
        for (Note note: notes) {
            File file_s = new File(Utils.getFileForUri(path), replaceSignChars(note.getTitle()) + ".txt");
            FileOutputStream stream;
            try {
                stream = new FileOutputStream(file_s);
                stream.write(note.toExportString().getBytes());
                stream.close();
            } catch (IOException e) {
                exportAlert(context, false, false);
                return;
            }
        }
        exportAlert(context, true, false);
    }

    /**
     * Сохранение заметки
     * @param note Экземпляр заметки
     * @param path Родительский пусть
     * @param context Контекст
     */
    public static void exportNote(Note note, Uri path, Context context) {
        File file_s = new File(Utils.getFileForUri(path), replaceSignChars(note.getTitle()) + ".txt");
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(file_s);
            stream.write(note.toExportString().getBytes());
            stream.close();
        } catch (IOException e) {
            exportAlert(context, false, true);
            return;
        }
        exportAlert(context, true, true);
    }

    /**
     * Сообщение результата экспорта
     * @param context Контекст
     * @param success Результат экспорта
     */
    private static void exportAlert(Context context, boolean success, boolean singleNote) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        if (success) {
            alert.setTitle(R.string.export2);
            alert.setMessage(singleNote ? R.string.export_success_single : R.string.export_success);
        } else {
            alert.setTitle(R.string.error);
            alert.setMessage(R.string.export_error);
        }
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    /**
     * Заменяет все знаки, отличние от алфавита и цифр на символ _
     * @param str Входная строка
     * @return Форматированная строка
     */
    private static String replaceSignChars(String str)
    {
        return str.replaceAll("\\W", "_");
    }
}
