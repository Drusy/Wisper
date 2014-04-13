#ifndef __include__Item__
#define __include__Item__

#include "cocos2d.h"
#include "Strategy.h"

USING_NS_CC;

class Item : public CCSprite
{
public:
    CREATE_FUNC(Item);
    
    Item();
    Item(int life);
    Item(int life, int x, int y);
    Item(int life, const CCPoint &point);
    
    virtual ~Item();
    virtual void execute();
    
    void changeStrategy(Strategy *strategy);
    void setLife(float life);
    void addLife(float life);
    void moveX(int x);
    void moveY(int y);
    void move(int x, int y);
    
    bool removeLife(int damages);
    float getLife();
    bool isAlive();
    
protected:
    float _life;
    Strategy *_strategy;
};

#endif
