package com.lyric.android.app.widget.moremenu;

import com.lyric.android.app.R;

public enum MenuItemType {
    DELETE(1, R.string.more_item_delete);// 删除

    int type;
    int valueId;

    MenuItemType(int type, int valueId) {
        this.type = type;
        this.valueId = valueId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }
}
