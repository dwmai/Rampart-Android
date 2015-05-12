package game.modes;

import java.util.LinkedList;

import game.CCannon;
import game.CGame;
import game.CShip;
import game.CSiegeWeapons;
import game.CTimer;
import game.players.CPlayer;
import game.sounds.CSounds;
import game.utils.CTimeUtil;

/**
 * @brief Mode where players fire cannonballs and destroy walls
 */
public class CBattleMode extends CMapMode {

	private int DBattleType;

	/**
	 * @brief Sets up the timer and cannons that are ready
	 * @param game
	 *            The game entering
	 */
	public void Enter(CGame game) {
		super.Enter(game);
		CTimer Timer = game.GameState().DTimer;
		Timer.DTimeout = CTimeUtil.MakeTimeoutSecondsInFuture(1);
		game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctReady);

		java.util.ArrayList<CPlayer> Players = game.GameState().DPlayers;
		for (CPlayer Player : Players) {
            if (Player.DIsAI) {
                Player.TryToPlaceShipAI(game, Player);
            }
            Player.DReadyCannons.clear();
            Player.DReadyShipCannons.clear();
		}

        DBattleType = 0;

		java.util.ArrayList<CCannon> Cannons = game.GameState().ConstructionMap().Cannons();
		for (CCannon cannon : Cannons) {
			CPlayer Player = cannon.GetPlayerOwner(game);
			if (Player != null) {
				Player.DReadyCannons.add(cannon);
			}
		}
        LinkedList<CShip> Ship = game.GameState().Units().Ship();
        for (CShip ship : Ship) {
            CPlayer Player = ship.GetPlayerOwner(game);
            if (Player != null) {
                Player.DReadyShipCannons.add(ship);
            }
        }

        LinkedList<CSiegeWeapons> SiegeWeapons = game.GameState().Units().SiegeWeapons();
        for (CSiegeWeapons weapon : SiegeWeapons) {
            if(weapon.IsMoving()) {
                game.GameState().Units().MovingSiege().add(weapon);
            }
        }
        for (CSiegeWeapons siegeWeapon : SiegeWeapons) {
            if (siegeWeapon.IsMoving()) {
                game.GameState().Units().MovingSiege().add(siegeWeapon);
            }
        }

    }

	/**
	 * @brief Determines if players are firing cannons and whether game should go to next mode
	 * @param game
	 *            The game updating
	 */
    public void Update(CGame game) {
        super.Update(game);
        game.GameState().DWind.WindUpdate(game.GameState().DRandomNumberGenerator.Random(), game.GameState().DRandomNumberGenerator.Random(), game.GameState().DRandomNumberGenerator.Random());
        if (DBattleType == 0) {
            if (CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) < 0) {
                DBattleType++;
                CTimer Timer = game.GameState().DTimer;
                Timer.DTimeout = CTimeUtil.MakeTimeoutSecondsInFuture(1);
                game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctAim);
            }
        } else if (DBattleType == 1) {
            if (CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) < 0) {
                DBattleType++;
                CTimer Timer = game.GameState().DTimer;
                Timer.DTimeout = CTimeUtil.MakeTimeoutSecondsInFuture(15);
                game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctFire);
            }
        } else if (DBattleType == 2) {
            for (CPlayer Player : game.GameState().DPlayers) {
                if (Player.ShouldTakePrimaryAction(game) && CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) >= 0) {
                    Player.FireNextCannon(game);
                } else if (Player.DIsAI && CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) >= 0) {
                    Player.FireNextCannon(game);
                }

            }

            LinkedList<CShip> Ship = game.GameState().Units().Ship();
            boolean Advance = true;
            for (int i = Ship.size() - 1; i >= 0; i--) {
                CShip ship = Ship.get(i);
                if (ship.IsAlive()) {
                    ship.Update(game);
                    Advance = true;
                } else {
                    game.GameState().Units().DestroyShip(ship);
                    Advance = false;
                }
                if (Advance) {
                }
            }

            LinkedList<CSiegeWeapons> SiegeWeapons = game.GameState().Units().SiegeWeapons();
            Advance = true;
            for (int i = SiegeWeapons.size() - 1; i >= 0; i--) {
                CSiegeWeapons weapon = SiegeWeapons.get(i);
                if (weapon.IsAlive()) {
                    weapon.Update(game);
                    Advance = true;
                } else {
                    game.GameState().Units().DestroySiege(weapon);
                    Advance = false;
                }
                if (Advance) {
                }
            }

            if (CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) < 0) {
                DBattleType++;
                CTimer Timer = game.GameState().DTimer;
                Timer.DTimeout = CTimeUtil.MakeTimeoutSecondsInFuture(2);
                game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctCeasefire);
            }
        } else if (DBattleType == 3) {
            boolean Advance = true;
            LinkedList<CShip> Ship = game.GameState().Units().Ship();
            for (CShip ship : Ship)
            {
                if (ship.IsAlive()) {
                    if (!(ship.IsMoving())) {
                        ship.Update(game);
                        Advance = true;
                    }
                } else {
                    game.GameState().Units().DestroyShip(ship);
                    Advance = false;
                }
                if (Advance) {
                }
            }
            if (CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) < 0 && game.GameState().DCannonballs.size() == 0 && game.GameState().DAnimations.size() == 0) {
                game.SwitchMode(new CBannerTransitionMode(game, "REBUILD WALLS TO STAY ALIVE", this, new CRebuildMode()));
            }
        }
    }

	/**
	 * @brief Draws the 3D map and the target reticules for the players
	 * @param game
	 *            The game drawing
	 */
	public void Draw(CGame game) {
		super.Draw3D(game);
		super.Draw(game);
		super.DrawTargetCursors(game);
	}
}
