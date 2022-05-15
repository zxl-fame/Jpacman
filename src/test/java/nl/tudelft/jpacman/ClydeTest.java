package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.ghost.Clyde;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClydeTest {
    private PacManSprites pacManSprites;
    private BoardFactory boardFactory;
    private GhostFactory ghostFactory;
    private PointCalculator pointCalculator;
    private LevelFactory levelFactory;
    private GhostMapParser ghostMapParser;
    private PlayerFactory playerFactory;

    @BeforeEach
    void PlayerObject() {
        pacManSprites = new PacManSprites();
        boardFactory = new BoardFactory(pacManSprites);
        ghostFactory = new GhostFactory(pacManSprites);
        pointCalculator = new DefaultPointCalculator();
        levelFactory = new LevelFactory(pacManSprites, ghostFactory, pointCalculator);
        playerFactory = new PlayerFactory(pacManSprites);
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /**
     * 测试Player和Clyde距离小于8
     */
    @Test
    void testTooClose() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "############", "#P       C##", "############"
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));
    }

    /**
     * 测试Player和Clyde距离大于8
     */
    @Test
    void testTooFar() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "############", "C         P#", "############"
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));

    }

    /**
     * 测试Clyde没有路径能接近Player
     */
    @Test
    void testNoPathBetweenPlayerAndClyde() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "######", "#P##C ", " ###  "
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }

    /**
     * 测试没有Player的情况
     */
    @Test
    void testNoPlayer() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "#####", "##C  ", "     "
            )
        );
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }
}
