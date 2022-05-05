class Settings(object):
    """docstring for Settings """

    def __init__(self):
        # initialize setting of game
        # screen setting
        self.game_start = False
        self.game_over = False

        self.screen_width = 1000
        self.screen_height = 600
        self.bg = 'images/bg.bmp'
        self.bgm = 'audio/bgm.mp3'
        self.shoot_au = 'audio/laser.wav'
        self.exp_au_1 = 'audio/exp_1.wav'
        self.exp_au_2 = 'audio/exp_2.mp3'
        self.lv_up = 'audio/lv_up.wav'
        self.score = 0
        self.judge_dis = 50

        self.ship_icon = 'images/plane.bmp'
        self.ship_icon_l = 'images/plane_l.bmp'
        self.ship_icon_r = 'images/plane_r.bmp'
        self.ship_speed_factor = 6
        self.ship_blood = 3

        self.bullet_icon = 'images/bullet_1.bmp'
        self.bullet_icon_1 = 'images/bullet_1.bmp'
        self.bullet_icon_2 = 'images/bullet_2.bmp'
        self.bullet_speed_factor = 5
        self.bullet_type = 'normal'
        self.bullet_damage = 1

        self.ammo_type = (1, 'A')
        self.ammo_num = 1

        self.enemy_icon = 'images/ufo.bmp'
        self.enemy_speed = 3
        self.enemy_speed_h = 5
        self.enemy_blood = 5
        self.enemy_num = 5
        self.enemy_score = 5

        self.buff_icon_1 = 'images/buff_1.bmp'
        self.buff_icon_2 = 'images/buff_2.bmp'
        self.buff_icon_3 = 'images/buff_3.bmp'
        self.buff_speed_factor = 2
