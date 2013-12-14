#include "Item.h"

Item::Item(int life) :
    _life(life)
{
}

Item::Item() :
    _life(0)
{
    
}

Item::Item(int life, int x, int y) :
    _life(life)
{
    setPositionX(x);
    setPositionY(y);
}

Item::Item(int life, const CCPoint &point) :
    _life(life)
{
    setPosition(point);
}

Item::~Item()
{
    
}

bool Item::removeLife(int damages)
{
    _life -= damages;
    
    return isAlive();
}

float Item::getLife()
{
    return _life;
}

void Item::setLife(float life)
{
    _life = life;
}

void Item::addLife(float life)
{
    _life += life;
}

void Item::changeStrategy(Strategy *strategy)
{
    _strategy = strategy;
}

void Item::move(int x, int y)
{
    setPositionX(getPositionX() + x);
    setPositionY(getPositionY() + y);
}

void Item::execute()
{
    _strategy->execute(this);
}

void Item::moveX(int x)
{
    move(x, 0);
}

void Item::moveY(int y)
{
    move(0, y);
}

bool Item::isAlive()
{
    return (_life > 0);
}