#ifndef __EndGameLayer_H__
#define __EndGameLayer_H__

#include "cocos2d.h"

USING_NS_CC;

class AppDelegate;

class EndGameLayer : public cocos2d::CCLayer
{
private:
	/// Rapport de la taille de l'�cran par rapport � celle des sprites.
	float _scale;
	/// Label �crivant le nom de la scene.
	CCLabelTTF *_labelName;

public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // implement the "static node()" method manually
    CREATE_FUNC(EndGameLayer);
    
    /// M�thode appel� � la fin de la s�quence.
    void endScene();
};

#endif // __EndGameLayer_H__
