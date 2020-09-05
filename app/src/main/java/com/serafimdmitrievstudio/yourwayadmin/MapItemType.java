package com.serafimdmitrievstudio.yourwayadmin;

class MapItemType {
    static final short Road = 1;
    static final short Point = 2;
    static final short Passage = 4;
    static final short Lift = 5;
    static final short Ramp = 6;
    static final short UndergroundPassage = 7;
    static final short OverheadPassage = 8;

    private short type;

    MapItemType(short type) {
        this.type =  type;
    };

    void setType(short Type) {
        type = Type;
    }

    short getType() {
        return type;
    }

}
