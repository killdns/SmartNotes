package xyz.danshin.smartnotes.view;


import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Класс-аниматор для recyclerView
 */
public class RvAnimator extends SlideInLeftAnimator {
    /**
     * Установка параметров аниматора
     */
    public RvAnimator()
    {
        setAddDuration(500);
        setRemoveDuration(500);
        setMoveDuration(500);
        setChangeDuration(750);
        this.setSupportsChangeAnimations(true);
    }

}
