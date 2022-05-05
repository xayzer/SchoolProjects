import pygame
import math


class Ship():
    def __init__(self, ai_settings, screen):
        # initialize spaceship and its location
        self.screen = screen
        self.ai_settings = ai_settings
        # load bmp image and get rectangle
        self.image = pygame.image.load(ai_settings.ship_icon)
        self.rect = self.image.get_rect()
        self.screen_rect = screen.get_rect()
        # put spaceship on the bottom of window
        self.rect.centerx = self.screen_rect.centerx
        self.rect.bottom = self.screen_rect.bottom
        self.ship_speed = ai_settings.ship_speed_factor

        self.moving_right = False
        self.moving_left = False
        self.moving_up = False
        self.moving_down = False

    def update(self, enteam, ammo, ai_setting):
        self.crash(enteam)
        self.up_grade(enteam.buff_list, ammo, ai_setting)
        if self.moving_right:
            if self.rect.centerx + self.ship_speed <= self.ai_settings.screen_width - 25:
                self.rect.centerx += self.ship_speed
        if self.moving_left:
            if self.rect.centerx - self.ship_speed >= 25:
                self.rect.centerx -= self.ship_speed
        if self.moving_up:
            if self.rect.bottom - self.ship_speed >= 50:
                self.rect.bottom -= self.ship_speed
        if self.moving_down:
            if self.rect.bottom + self.ship_speed <= self.ai_settings.screen_height:
                self.rect.bottom += self.ship_speed

    def draw_ship(self):
        # buld the spaceship at the specific location
        self.screen.blit(self.image, self.rect)

    def up_grade(self, buff_list, ammo, ai_setting):
        for buff in buff_list:
            bol = False
            x = self.rect.centerx - buff.x
            y = self.rect.bottom - buff.y
            dis = math.sqrt(x * x + y * y)
            if dis < self.ai_settings.judge_dis:
                buff.effect(self, ammo, ai_setting)
                pygame.mixer.Sound(self.ai_settings.lv_up).play()
                bol = True
            if bol:
                buff_list.remove(buff)

    def crash(self, enteam):
        for ey in enteam.team:
            bol = False
            x = self.rect.centerx - ey.x
            y = self.rect.bottom - ey.y
            dis = math.sqrt(x * x + y * y)
            if dis < self.ai_settings.judge_dis:
                self.ai_settings.ship_blood -= 1
                enteam.team.remove(ey)
                pygame.mixer.Sound(ey.ai_setting.exp_au_1).play()