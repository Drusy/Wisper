#ifndef __header__Missile__
#define __header__Missile__

#include "cocos2d.h"
#include "Item.h"

USING_NS_CC;

class Missile : public Item
{
public:
    Missile(int life, int damages);
    ~Missile();
    
    void setDamages(int damages);
    bool hit(Missile &aim);
    
private:
    float _damages;
};

#endif
