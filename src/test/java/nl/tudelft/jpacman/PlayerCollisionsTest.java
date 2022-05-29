package nl.tudelft.jpacman;

import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerCollisions;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class PlayerCollisionsTest {
    private PlayerCollisions playerCollisions;
    private PointCalculator pointCalculator;


    @BeforeEach
    void setup() {
        pointCalculator = mock(PointCalculator.class);
        playerCollisions = new PlayerCollisions(pointCalculator);

    }

    /**
     * 规则1：Ghost移动撞到另一个Ghost
     */
    @Test
    void ghostCollideOtherGhost() {
        Ghost ghost1 = mock(Ghost.class);
        Ghost ghost2 = mock(Ghost.class);
        playerCollisions.collide(ghost1, ghost2);
        verifyNoInteractions(ghost1, ghost2);
    }

    /**
     * 规则2：Ghost移动撞到Player
     */
    @Test
    void ghostCollidePlayer() {
        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);

        playerCollisions.collide(ghost, player);
        verify(pointCalculator).collidedWithAGhost(player, ghost);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }

    /**
     * 规则3：Ghost移动撞到Pellet
     */
    @Test
    void ghostCollidePellet() {
        Ghost ghost = mock(Ghost.class);
        Pellet pellet = mock(Pellet.class);
        playerCollisions.collide(ghost, pellet);
        verifyNoInteractions(ghost, pellet);
    }

    /**
     * 规则4：Player移动撞到Ghost
     */
    @Test
    void playerCollideGhost() {
        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);

        playerCollisions.collide(player, ghost);
        verify(pointCalculator).collidedWithAGhost(player, ghost);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }

    /**
     * 规则5：Player移动撞到Pellet
     */
    @Test
    void playerCollideOnPellet() {
        Player player = mock(Player.class);
        Pellet pellet = mock(Pellet.class);
        playerCollisions.collide(player, pellet);
        verify(pointCalculator).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
    }
}
