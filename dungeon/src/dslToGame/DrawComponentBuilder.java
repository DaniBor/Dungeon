package dslToGame;

import core.Entity;
import core.components.DrawComponent;
import semanticanalysis.types.DSLTypeAdapter;
import semanticanalysis.types.DSLTypeMember;

import java.io.IOException;

public class DrawComponentBuilder {
    @DSLTypeAdapter
    public static DrawComponent buildDrawComponent(
        @DSLTypeMember(name="entity") Entity entity,
        @DSLTypeMember(name="path") String path) {
        try {
            return new DrawComponent(entity, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
