import pygame
from setting import Settings
from ship import Ship
import game_functions as gf
from bullet import Ammo
from enemy import EnemyTeam


def run_game():
    # initialize game and create a dispaly object
    pygame.init()
    ai_setting = Settings()
    screen = pygame.display.set_mode((ai_setting.screen_width, ai_setting.screen_height))
    ship = Ship(ai_setting, screen)
    ammo = Ammo(ai_setting)
    enteam = EnemyTeam(ai_setting, screen)
    pygame.display.set_caption("Alien Invasion")
    i = 1
    pygame.mixer.music.load(ai_setting.bgm)
    pygame.mixer.music.play(-1)
    while not ai_setting.game_start:
        gf.start_game(ai_setting,screen)
    # game_loop
    while not ai_setting.game_over:
        # supervise keyboard and mouse item
        gf.check_events(ai_setting, screen, ship, ammo)
        # ship.update()
        gf.update_screen(ai_setting, screen, ship, ammo, enteam)
        # visualiaze the window
    while ai_setting.game_over:
        gf.quit_game()


run_game()
