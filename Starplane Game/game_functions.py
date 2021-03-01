import sys
import pygame
from bullet import Bullet


def check_keydown_events(event, ship, ammo, ai_setting, screen):
    if event.key == pygame.K_RIGHT:
        # move right
        ship.moving_right = True
        ship.image = pygame.image.load(ai_setting.ship_icon_r)
    if event.key == pygame.K_LEFT:
        # move left
        ship.moving_left = True
        ship.image = pygame.image.load(ai_setting.ship_icon_l)
    if event.key == pygame.K_UP:
        # move up
        ship.moving_up = True
    if event.key == pygame.K_DOWN:
        # move down
        ship.moving_down = True
    if event.key == pygame.K_SPACE:
        bol = ammo.add_ammo(Bullet(ai_setting, screen, ship))
        if bol:
            pygame.mixer.Sound(ai_setting.shoot_au).play()


def check_keyup_events(event, ship):
    if event.key == pygame.K_RIGHT:
        ship.moving_right = False
    if event.key == pygame.K_LEFT:
        ship.moving_left = False
    if event.key == pygame.K_UP:
        ship.moving_up = False
    if event.key == pygame.K_DOWN:
        ship.moving_down = False


def check_events(ai_setting, screen, ship, ammo):
    # respond to keyboard and mouse item
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            sys.exit()
        elif event.type == pygame.KEYDOWN:
            check_keydown_events(event, ship, ammo, ai_setting, screen)
        elif event.type == pygame.KEYUP:
            check_keyup_events(event, ship)
            ship.image = pygame.image.load(ai_setting.ship_icon)


def update_screen(ai_setting, screen, ship, ammo, enteam):
    bg = pygame.image.load(ai_setting.bg)
    screen.blit(bg, (0, 0))
    ship.update(enteam, ammo, ai_setting)
    ship.draw_ship()
    ammo.update_ammo()
    enteam.show_enemy()
    enteam.draw_buff()
    enteam.update_enemy(ammo)

    show_score(ai_setting, screen)
    show_blood(ai_setting, screen)
    show_ammo(ammo, screen)
    end_game(ai_setting, screen)
    # visualiaze the window
    pygame.display.update()


def show_score(ai_setting, screen):
    font = pygame.font.SysFont('Jokerman', 20)
    text = f"Score:{ai_setting.score}"
    score_render = font.render(text, True, (0, 255, 0))
    screen.blit(score_render, (10, 10))


def show_blood(ai_setting, screen):
    font = pygame.font.SysFont('Jokerman', 20)
    text = f"Life x{ai_setting.ship_blood}"
    blood_render = font.render(text, True, (0, 255, 0))
    screen.blit(blood_render, (10, 40))


def show_ammo(ammo, screen):
    font = pygame.font.SysFont('Jokerman', 20)
    text = f"Ammo x{ammo.ammo_num - len(ammo.ammo_clip)}"
    ammo_render = font.render(text, True, (0, 255, 0))
    screen.blit(ammo_render, (10, 70))


def end_game(ai_setting, screen):
    if ai_setting.ship_blood <= 0:
        file = open('record/record.txt', 'a+')
        file.close()
        file = open('record/record.txt', 'r+')
        line = file.readline()
        try:
            if line == '':
                file.write(str(ai_setting.score))
                best_score = ai_setting
            elif int(line) < ai_setting.score:
                best_score = ai_setting.score
                file.close()
                file = open('record/record.txt', 'w')
                file.write(str(ai_setting.score))
            else:
                best_score = int(line)
        finally:
            file.close()
        font = pygame.font.SysFont('Jokerman', 50)
        text1 = f"Game Over"
        sign_render = font.render(text1, True, (255, 0, 0))
        screen.blit(sign_render, (350, 150))
        text1 = f"Your  Score:  {ai_setting.score}"
        sign_render = font.render(text1, True, (255, 0, 0))
        screen.blit(sign_render, (280, 220))
        text1 = f"Best  Score:  {best_score}"
        sign_render = font.render(text1, True, (255, 0, 0))
        screen.blit(sign_render, (280, 290))

        font = pygame.font.SysFont('Jokerman', 30)
        text1 = f"press 'x' button to quit game"
        sign_render = font.render(text1, True, (255, 0, 0))
        screen.blit(sign_render, (300, 370))
        ai_setting.game_over = True


def quit_game():
    for event in pygame.event.get():
        if event.type == pygame.KEYDOWN and event.key == pygame.K_x:
            pygame.quit()
            quit()
        if event.type == pygame.QUIT:
            sys.exit()


def start_game(ai_setting, screen):
    bg = pygame.image.load(ai_setting.bg)
    screen.blit(bg, (0, 0))
    font = pygame.font.SysFont('Jokerman', 50)
    text1 = f"Planet Savior"
    sign_render = font.render(text1, True, (100, 255, 0))
    screen.blit(sign_render, (300, 200))
    font = pygame.font.SysFont('Jokerman', 30)
    text1 = f"press 'x' button to start game"
    sign_render = font.render(text1, True, (100, 255, 0))
    screen.blit(sign_render, (280, 300))
    pygame.display.update()
    for event in pygame.event.get():
        if event.type == pygame.KEYDOWN and event.key == pygame.K_x:
            ai_setting.game_start = True
        if event.type == pygame.QUIT:
            sys.exit()
