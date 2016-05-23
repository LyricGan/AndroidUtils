package com.lyric.android.app.widget.moremenu;

public class MenuItemEntity {
    /** 菜单分类 */
    private MenuItemType itemType;
    /** 菜单文本 */
    private String text;
    /** 菜单项文本颜色值 */
    private int colorId;

    public MenuItemType getItemType() {
        return itemType;
    }

    public void setItemType(MenuItemType itemType) {
        this.itemType = itemType;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
