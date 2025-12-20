package net.lghast.jiagu.utils.lzh;

import java.util.ArrayList;
import java.util.List;

public enum CharacterStructure {
    HORIZONTAL("   ;AB ;   "),
    VERTICAL(" A ; B ;   "),
    HORIZONTAL_LONG("   ;ABC;   "),
    VERTICAL_LONG(" A ; B ; C "),
    TRIANGLE("A  ;BC ;   "),
    TRIANGLE_RIGHT(" A ;BC ;   "),
    TRIANGLE_INVERT("   ;AB ;C  "),
    TRIANGLE_RIGHT_INVERT("   ;AB ; C "),
    RHOMBUS(" A ;B C; D "),
    T_SHAPE("   ;ABC; D "),
    T_SHAPE_LONG("ABC; D ; E "),
    T_SHAPE_INVERT(" A ;BCD;   "),
    T_SHAPE_HORIZONTAL("A  ;BC ;D  "),
    L_SHAPE(" A ; B ; CD"),
    X_SHAPE("A B; C ;D E"),
    RECTANGLE("ABC;DEF;   "),
    ARROW(" A ;B C;   "),
    ARROW_INVERT("   ;A B; C "),
    BIAS("   ;A  ; B "),
    SQUARE("   ;AB ;CD "),
    CROSS(" A ;BCD; E "),
    SAME("   ; A ;   ");

    final String format;
    CharacterStructure(String format) {
        this.format = format;
    }

    public List<String> getComponentList(List<String> components) {
        List<String> result = new ArrayList<>();

        String[] rows = format.split(";");

        for (String row : rows) {
            for (int i = 0; i < row.length(); i++) {
                char c = row.charAt(i);
                if (c == ' ') {
                    result.add(null);
                } else if (c >= 'A' && c <= 'Z') {
                    int index = c - 'A';
                    if (index < components.size()) {
                        result.add(components.get(index));
                    } else {
                        result.add(null);
                    }
                }
            }
        }

        return result;
    }
}
