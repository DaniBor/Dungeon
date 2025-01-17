package core.level;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import core.LevelManager;
import core.level.elements.ILevel;
import core.level.generator.IGenerator;
import core.level.utils.Coordinate;
import core.level.utils.DesignLabel;
import core.level.utils.LevelElement;
import core.level.utils.LevelSize;
import core.utils.Point;
import core.utils.components.draw.Painter;
import core.utils.components.draw.PainterConfig;
import core.utils.components.draw.TextureMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TextureMap.class})
public class TileLevelAPITest {

    private LevelManager api;
    private IGenerator generator;
    private Texture texture;
    private TextureMap textureMap;
    private Painter painter;
    private SpriteBatch batch;
    private IOnLevelLoader onLevelLoader;
    private ILevel level;

    @Before
    public void setup() {
        batch = Mockito.mock(SpriteBatch.class);

        texture = Mockito.mock(Texture.class);
        textureMap = Mockito.mock(TextureMap.class);
        PowerMockito.mockStatic(TextureMap.class);
        when(TextureMap.instance()).thenReturn(textureMap);
        when(textureMap.textureAt(anyString())).thenReturn(texture);

        painter = Mockito.mock(Painter.class);
        generator = Mockito.mock(IGenerator.class);
        onLevelLoader = Mockito.mock(IOnLevelLoader.class);
        level = Mockito.mock(TileLevel.class);
        api = new LevelManager(batch, painter, generator, onLevelLoader);
    }

    @Test
    public void test_loadLevel() {
        when(generator.level(Mockito.any(), Mockito.any())).thenReturn(level);
        api.loadLevel(LevelSize.SMALL, DesignLabel.DEFAULT);
        verify(generator).level(DesignLabel.DEFAULT, LevelSize.SMALL);
        Mockito.verifyNoMoreInteractions(generator);
        verify(onLevelLoader).onLevelLoad();
        Mockito.verifyNoMoreInteractions(onLevelLoader);
        assertEquals(level, api.currentLevel());
    }

    @Test
    public void test_loadLevel_noParameter() {
        when(generator.level(Mockito.any(), Mockito.any())).thenReturn(level);
        api.loadLevel();
        verify(generator).level(Mockito.any(), Mockito.any());
        Mockito.verifyNoMoreInteractions(generator);
        verify(onLevelLoader).onLevelLoad();
        Mockito.verifyNoMoreInteractions(onLevelLoader);
        assertEquals(level, api.currentLevel());
    }

    @Test
    public void test_loadLevel_withDesign_noSize() {
        when(generator.level(eq(DesignLabel.DEFAULT), any())).thenReturn(level);
        api.loadLevel(DesignLabel.DEFAULT);
        verify(generator).level(eq(DesignLabel.DEFAULT), any());
        Mockito.verifyNoMoreInteractions(generator);
        verify(onLevelLoader).onLevelLoad();
        Mockito.verifyNoMoreInteractions(onLevelLoader);
        assertEquals(level, api.currentLevel());
    }

    @Test
    public void test_loadLevel_noDesign_WithSize() {
        when(generator.level(any(), eq(LevelSize.SMALL))).thenReturn(level);
        api.loadLevel(LevelSize.SMALL);
        verify(generator).level(any(), eq(LevelSize.SMALL));
        Mockito.verifyNoMoreInteractions(generator);
        verify(onLevelLoader).onLevelLoad();
        Mockito.verifyNoMoreInteractions(onLevelLoader);
        assertEquals(level, api.currentLevel());
    }

    @Test
    public void test_update() {
        String textureT1 = "dummyPath1";
        String textureT2 = "dummyPath2";
        String textureT3 = "dummyPath3";
        String textureT4 = "dummyPath4";
        Coordinate coordinateT1 = new Coordinate(0, 0);
        Coordinate coordinateT2 = new Coordinate(0, 1);
        Coordinate coordinateT3 = new Coordinate(1, 0);
        Coordinate coordinateT4 = new Coordinate(1, 1);
        LevelElement elementT1 = LevelElement.WALL;
        LevelElement elementT2 = LevelElement.EXIT;
        LevelElement elementT3 = LevelElement.WALL;
        LevelElement elementT4 = LevelElement.SKIP;
        Tile[][] layout = new Tile[2][2];
        layout[0][0] = Mockito.mock(Tile.class);
        when(layout[0][0].levelElement()).thenReturn(elementT1);
        when(layout[0][0].texturePath()).thenReturn(textureT1);
        when(layout[0][0].position()).thenReturn(coordinateT1.toPoint());
        layout[0][1] = Mockito.mock(Tile.class);
        when(layout[0][1].levelElement()).thenReturn(elementT2);
        when(layout[0][1].texturePath()).thenReturn(textureT2);
        when(layout[0][1].position()).thenReturn(coordinateT2.toPoint());
        layout[1][0] = Mockito.mock(Tile.class);
        when(layout[1][0].levelElement()).thenReturn(elementT3);
        when(layout[1][0].texturePath()).thenReturn(textureT3);
        when(layout[1][0].position()).thenReturn(coordinateT3.toPoint());
        layout[1][1] = Mockito.mock(Tile.class);
        when(layout[1][1].levelElement()).thenReturn(elementT4);
        when(layout[1][1].texturePath()).thenReturn(textureT4);
        when(layout[1][1].position()).thenReturn(coordinateT4.toPoint());

        when(level.layout()).thenReturn(layout);

        api.level(level);
        api.update();

        verify(level).layout();
        verifyNoMoreInteractions(level);

        verify(layout[0][0]).levelElement();
        verify(layout[0][0]).texturePath();
        verify(layout[0][0]).position();
        // for some reason mocktio.verify can't compare the points of the tile correctly
        verify(painter, times(3))
                .draw(any(Point.class), any(String.class), any(PainterConfig.class));
        verifyNoMoreInteractions(layout[0][0]);

        verify(layout[0][1]).levelElement();
        verify(layout[0][1]).texturePath();
        verify(layout[0][1]).position();
        // for some reason mocktio.verify can't compare the points of the tile correctly
        verify(painter, times(3))
                .draw(any(Point.class), any(String.class), any(PainterConfig.class));
        verifyNoMoreInteractions(layout[0][1]);
        verify(layout[1][0]).levelElement();
        verify(layout[1][0]).texturePath();
        verify(layout[1][0]).position();
        // for some reason mocktio.verify can't compare the points of the tile correctly
        verify(painter, times(3))
                .draw(any(Point.class), any(String.class), any(PainterConfig.class));
        verifyNoMoreInteractions(layout[1][0]);

        // do not draw skip tiles
        verify(layout[1][1]).levelElement();
        verifyNoMoreInteractions(layout[1][1]);
        verifyNoMoreInteractions(painter);
    }

    @Test
    public void test_setLevel() {
        api.level(level);
        Mockito.verifyNoInteractions(generator);
        verify(onLevelLoader).onLevelLoad();
        Mockito.verifyNoMoreInteractions(onLevelLoader);
        assertEquals(level, api.currentLevel());
    }
}
