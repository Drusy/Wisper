#include "Missile.h"

Missile::Missile(int life, int damages) :
    Item(life),
    _damages(damages)
{
    
}

Missile::~Missile()
{
}

void Missile::setDamages(int damages)
{
    _damages = damages;
}

bool Missile::hit(Missile &aim)
{
    return aim.removeLife(_damages);
}