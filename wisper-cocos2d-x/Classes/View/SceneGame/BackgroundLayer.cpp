#include "BackgroundLayer.h"
#include "View/Gestion/GestionScene.h"

using namespace cocos2d;

void adjustParticle2(CCParticleSystemQuad *p, float factor) 
{
	p->setStartSize(p->getStartSize() * factor);
	p->setStartSizeVar(p->getStartSizeVar() * factor);
	p->setEndSize(p->getEndSize() * factor);
	p->setEndSizeVar(p->getEndSizeVar() * factor);
	p->setSpeed(p->getSpeed() * factor);
	p->setSpeedVar(p->getSpeedVar() * factor);
	//p->setEmissionRate(p->getEmissionRate() / factor);
}

// on "init" you need to initialize your instance
bool BackgroundLayer::init()
{
	/// Appel de la classe parent.
    if ( !CCLayer::init() )
    {
        return false;
    }

    /// On désactive l'accelerometre et la click pour cette scene.
    this->setAccelerometerEnabled(false);
    this->setTouchEnabled(false);

    /// On récupère la taille de l'écran et calcul du rapport.
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	_scale = winSize.height/450.0f;

	/// Création d'un label à afficher.
	//_labelName = CCLabelTTF::create("BackgroundLayer", "Arial", 60 * _scale);

	/// On ajoute les images au layer et on les masque.
	//this->addChild(_labelName, 0);

	/// On créé et on ajoute les séquences d'animations pour les deux images.	
	//_labelName->runAction(CCSequence::create(CCFadeOut::create(0.0f), /// On le fait disparaitre
	//										CCMoveTo::create(0,ccp(winSize.width * 0.5f, winSize.height * 0.4f)), /// On le place ou il faut.
	//										CCDelayTime::create(0.5f), /// On attend une demi seconde.
	//										CCFadeIn::create(4.0f), /// On le faut apparaitre en 2 secondes.
	//										NULL));

	CCParticleSystemQuad *stars = CCParticleSystemQuad::create("particles/backgrounds/stars.plist");
	stars->setPosition(ccp(winSize.width + 20, winSize.height / 2.0));
	stars->setPosVar(ccp(0, winSize.width / 2));
	//adjustParticle2(stars, _scale);
	BackgroundLayer::addChild(stars);

	this->scheduleUpdate();

    return true;
}

void BackgroundLayer::update(float dt)
{
}


void BackgroundLayer::endScene()
{
	GestionScene::GetInstance()->changeScene(1);
}
