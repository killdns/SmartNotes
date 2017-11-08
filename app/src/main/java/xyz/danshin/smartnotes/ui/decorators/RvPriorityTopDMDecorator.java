package xyz.danshin.smartnotes.ui.decorators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.controller.ActivityController;

/**
 * Класс-декоратор для элементов приоритета заметок recyclerView. Добавляет отступ слева
 */
public class RvPriorityTopDMDecorator extends RecyclerView.ItemDecoration {

    public RvPriorityTopDMDecorator(Context context) {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        params.setMargins((int) ActivityController.getBaseActivity().getResources().getDimension(R.dimen.rv_top_menu_priority_margin_left), 0, 0, 0);
    }
}