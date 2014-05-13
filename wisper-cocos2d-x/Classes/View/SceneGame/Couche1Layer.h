#ifndef __Couche1Layer_H__
#define __Couche1Layer_H__

#include "cocos2d.h"

USING_NS_CC;

class AppDelegate;

class Couche1Layer : public cocos2d::CCLayer
{
private:
	/// Rapport de la taille de l'écran par rapport à celle des sprites.
	float _scale;
	/// Label écrivant le nom de la scene.
	CCLabelTTF *_labelName;

	CCSize _winSize;

	/// Particles
	CCParticleSystemQuad* _feuFollet;
	CCParticleSystemQuad* _soul;
	CCParticleSystemQuad* _soulBis;
	CCParticleSystemQuad* _missilSoul;

	CCPoint _newPosition;
	CCPoint _soulPosition;
	CCPoint _soulPositionBis;
	float _moveFactor;
	float _time;
	float _yAcc;
	float _xAcc;

	int _index;
	bool _isRespondingToSwip;

public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // implement the "static node()" method manually
    CREATE_FUNC(Couche1Layer);

    void respondToSwipe();
    void swipe(int index);
    void nextSwipe();

	void update(float dt);
	void updateSoulsPositions(float dt);
	void androidUpdate(float dt);
	void windowsUpdate(float dt);
	void initParticles();
};

#endif // __Couche1Layer_H__
