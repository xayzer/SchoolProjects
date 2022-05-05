import pygame
import random


class Buff():
    def __init__(self, ai_settings, screen, enemy):
        self.screen = screen
        self.ai_settings = ai_settings
        self.image = None
        self.x = enemy.x
        self.y = enemy.y
        self.type = None
        self.horizon_dir = 'left'
        self.vertical_dir = 'up'
        self.speed_factor = ai_settings.buff_speed_factor
        self.select_type()
        self.wh_remove = 600

    def update(self):
        self.wh_remove -= 1
        if self.y <= 0:
            self.vertical_dir = 'down'
        if self.y >= self.ai_settings.screen_height:
            self.vertical_dir = 'up'
        if self.x <= 0:
            self.horizon_dir = 'right'
        if self.x >= self.ai_settings.screen_width:
            self.horizon_dir = 'left'

        if self.vertical_dir == 'down':
            self.y += self.speed_factor
        elif self.vertical_dir == 'up':
            self.y -= self.speed_factor
        if self.horizon_dir == 'left':
            self.x -= self.speed_factor
        elif self.horizon_dir == 'right':
            self.x += self.speed_factor

    def draw_buff(self):
        """Draw the bullet to the screen."""
        self.screen.blit(self.image, (self.x, self.y))

    def effect(self, ship, ammo, ai_setting):
        if self.type == 'change_ammo':
            ai_setting.bullet_icon = ai_setting.bullet_icon_2
            ai_setting.bullet_type = 'pen_bullet'
        elif self.type == 'ship_upgrade':
            ship.ship_speed += 3
            ai_setting.bullet_damage += 1
            ammo.ammo_num += 1
        elif self.type == 'extra_life':
            ai_setting.ship_blood += 1

    def select_type(self):
        ran_num = random.randint(0, 100) % 3
        if ran_num == 0:
            self.type = 'ship_upgrade'
            self.image = pygame.image.load(self.ai_settings.buff_icon_1)
        elif ran_num == 1:
            self.type = 'extra_life'
            self.image = pygame.image.load(self.ai_settings.buff_icon_2)
        elif ran_num == 2:
            self.type = 'change_ammo'
            self.image = pygame.image.load(self.ai_settings.buff_icon_3)
