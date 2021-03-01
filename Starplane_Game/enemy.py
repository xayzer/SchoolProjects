import pygame
import random
import math
from buff import Buff


class Enemy():
    def __init__(self, ai_setting, screen):
        self.screen = screen
        self.ai_setting = ai_setting
        self.image = pygame.image.load(ai_setting.enemy_icon)
        self.enemy_speed = ai_setting.enemy_speed
        self.x = random.randint(100, 1000)
        self.y = random.randint(100, 400)
        self.enemy_blood = ai_setting.enemy_blood
        self.enemy_score = self.ai_setting.enemy_score
        self.direction = 'left' if self.x >= self.ai_setting.screen_width / 2 else 'right'

    def draw_enemy(self):
        self.screen.blit(self.image, (self.x, self.y))

    def update(self):
        if self.direction == 'left':
            if self.x - self.enemy_speed >= 0:
                self.x -= self.enemy_speed
            else:
                if self.y + self.enemy_speed <= self.ai_setting.screen_height:
                    self.y += self.enemy_speed * self.ai_setting.enemy_speed_h
                    self.direction = 'right'
        elif self.direction == 'right':
            if self.x + self.enemy_speed+50 <= self.ai_setting.screen_width:
                self.x += self.enemy_speed
            else:
                if self.y + self.enemy_speed <= self.ai_setting.screen_height:
                    self.y += self.enemy_speed * self.ai_setting.enemy_speed_h
                    self.direction = 'left'

    def cal_distance(self, bullet):
        x = self.x - bullet.x
        y = self.y - bullet.y
        dis = math.sqrt(x * x + y * y)
        if dis < self.ai_setting.judge_dis:
            self.enemy_blood -= bullet.damage
            pygame.mixer.Sound(bullet.ai_setting.exp_au_1).play()
            return True
        else:
            return False


class EnemyTeam():
    def __init__(self, ai_setting, screen):
        self.ai_setting = ai_setting
        self.screen = screen
        self.enemy_num = ai_setting.enemy_num
        self.team = []
        self.buff_list = []
        self.gen_enemy()

    def next_level(self):
        if len(self.team) == 0:
            self.enemy_num += 1
            self.ai_setting.enemy_blood += 10
            self.ai_setting.enemy_speed += 2
            self.ai_setting.enemy_score += 5
            self.gen_enemy()

    def gen_enemy(self):
        for i in range(0, self.enemy_num):
            self.team.append(Enemy(self.ai_setting, self.screen))

    def show_enemy(self):
        for ey in self.team:
            ey.draw_enemy()

    def update_enemy(self, ammo):
        self.hit_judge(ammo)
        self.remove_enemy()
        self.next_level()
        self.remove_buff()

        for buff in self.buff_list:
            buff.update()
        for ey in self.team:
            ey.update()

    def remove_enemy(self):
        for ey in self.team:
            if ey.enemy_blood <= 0:
                self.team.remove(ey)
                self.ai_setting.score += ey.enemy_score
                self.gen_buff(ey)
            if ey.y >= ey.ai_setting.screen_height:
                self.team.remove(ey)
                self.ai_setting.ship_blood -= 1

    def hit_judge(self, ammo):
        for ey in self.team:
            for bullet in ammo.ammo_clip:
                bol = ey.cal_distance(bullet)
                if bol and bullet.type != 'pen_bullet':
                    ammo.unload_bullet(bullet)

    def gen_buff(self, enemy):
        x = random.randint(0, 100) % 4
        if x == 0:
            buff = Buff(self.ai_setting, self.screen, enemy)
            self.buff_list.append(buff)

    def draw_buff(self):
        for buff in self.buff_list:
            buff.draw_buff()

    def remove_buff(self):
        for buff in self.buff_list:
            if buff.wh_remove <= 0:
                self.buff_list.remove(buff)
