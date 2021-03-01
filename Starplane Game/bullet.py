import pygame
from pygame.sprite import Sprite


class Bullet(Sprite):
    """A class to manage bullets fired from the ship."""

    def __init__(self, ai_setting, screen, ship):
        """Create a bullet object, at the ship's current position."""
        super().__init__()
        self.screen = screen
        self.ai_setting = ai_setting
        # Create bullet rect at (0, 0),then set correct position.
        self.image = pygame.image.load(ai_setting.bullet_icon)
        self.x = ship.rect.centerx - 10
        self.y = ship.rect.bottom - 10
        # Store a decimal value for the bullet's position.
        self.speed_factor = ai_setting.bullet_speed_factor
        self.damage = ai_setting.bullet_damage
        self.type = ai_setting.bullet_type

    def update(self):
        """Move the bullet up the screen. """
        # Update the decimal position of the bullet.
        self.y -= self.speed_factor
        # Update the rect position.

    def draw_bullet(self):
        """Draw the bullet to the screen."""
        self.screen.blit(self.image, (self.x, self.y))


class Ammo():
    def __init__(self, ai_setting):
        self.ammo_type = ai_setting.ammo_type
        self.ammo_num = ai_setting.ammo_num
        self.ammo_clip = []

    def add_ammo(self, bt):
        if len(self.ammo_clip) < self.ammo_num:
            self.ammo_clip.append(bt)
            return  True
        else:
            return False

    def remove_ammo(self):
        for bt in self.ammo_clip:
            if bt.y <= 0:
                self.unload_bullet(bt)
            bt.update()
            bt.draw_bullet()

    def show_ammo(self):
        for bt in self.ammo_clip:
            bt.draw_bullet()


    def update_ammo(self):
        self.remove_ammo()
        for bt in self.ammo_clip:
            bt.update()

    def unload_bullet(self,bullet):
        self.ammo_clip.remove(bullet)