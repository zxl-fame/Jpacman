package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class MapParserTest {
    private MapParser mapParser;
        private final LevelFactory levelFactory = mock(LevelFactory.class);
        private final BoardFactory boardFactory = mock(BoardFactory.class);

        @BeforeEach
        void init() {
            mapParser = new MapParser(levelFactory, boardFactory);
        when(boardFactory.createGround()).thenReturn(mock(Square.class));
        when(boardFactory.createWall()).thenReturn(mock((Square.class)));
        when(levelFactory.createGhost()).thenReturn(mock(Ghost.class));
        when(levelFactory.createPellet()).thenReturn(mock(Pellet.class));
    }

    @Test
    @DisplayName("读取一个存在的文件")
    void parseMap1() throws Exception{
        String file="/simplemapOfPlayer.txt";
        mapParser.parseMap(file);
        verify(boardFactory,times(3)).createWall();
        verify(boardFactory,times(3)).createGround();
        verify(levelFactory,times(1)).createPellet();
    }

    @Test
    @DisplayName("读取另一个存在的文件")
    void parseMap2() throws Exception{
        String file="/simplemap.txt";
        mapParser.parseMap(file);
        verify(boardFactory,times(2)).createWall();
        verify(boardFactory,times(4)).createGround();
        verify(levelFactory,times(1)).createGhost();
        verify(levelFactory,times(1)).createPellet();
    }

    @Test
    @DisplayName("读取null文件名")
    void parseMap3() {
        assertThatThrownBy(()->{
            mapParser.parseMap((String)null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("读取文件不存在")
    void parseMap4(){
        String file="/UnExist.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class);
    }

    @Test
    @DisplayName("读取不能识别的地图")
    void parseMap5() throws Exception{
        String file="/unrecognizedcharmap.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class);
    }
}
