#include "Couche1Layer.h"
#include "View/Gestion/GestionScene.h"

using namespace cocos2d;

// on "init" you need to initialize your instance
bool Couche1Layer::init()
{
	/// Appel de la classe parent.
    if ( !CCLayer::init() )
    {
        return false;
    }

    /// On désactive l'accelerometre et la click pour cette scene.
    this->setAccelerometerEnabled(false);
    this->setTouchEnabled(false);

	/// Update
	this->scheduleUpdate();
	this->setKeypadEnabled(true);

    /// On récupère la taille de l'écran et calcul du rapport.
    _winSize = CCDirector::sharedDirector()->getWinSize();
	_scale = _winSize.height/720.0f;

	/// Création d'un label à afficher.
	//_labelName = CCLabelTTF::create("Couche1Layer", "Arial", 60 * _scale);

	/// On ajoute les images au layer et on les masque.
	//this->addChild(_labelName, 0);

	/// On créé et on ajoute les séquences d'animations pour les deux images.	
	//_labelName->runAction(CCSequence::create(CCFadeOut::create(0.0f), /// On le fait disparaitre
	//										CCMoveTo::create(0,ccp(_winSize.width * 0.5f, _winSize.height * 0.5f)), /// On le place ou il faut.
	//										CCDelayTime::create(0.5f), /// On attend une demi seconde.
	//										CCFadeIn::create(2.5f), /// On le faut apparaitre en 2 secondes.
	//										NULL));

	initParticles();

	_yAcc = 0;
	_isRespondingToSwip = false;
	_index = 0;

    return true;
}

void adjustParticle(CCParticleSystemQuad *p, float factor) 
{
	p->setStartSize(p->getStartSize() * factor);
	p->setStartSizeVar(p->getStartSizeVar() * factor);
	p->setEndSize(p->getEndSize() * factor);
	p->setSpeed(p->getSpeed() * factor);
	p->setSpeedVar(p->getSpeedVar() * factor);
	p->setEmissionRate(p->getEmissionRate() / factor);
}

void Couche1Layer::initParticles()
{
	// Wisper
	_feuFollet = CCParticleSystemQuad::create("particles/players/blue_wisper.plist");
	//adjustParticle(_feuFollet, _scale);
	this->addChild(_feuFollet);

	_feuFollet->setPosition(_winSize.width / 2, _winSize.height / 2);
	_feuFollet->getContentSize();

	_moveFactor = 1;
	_time = 0;

	// Soul
	_soul = CCParticleSystemQuad::create("particles/players/blue_soul.plist");
	//adjustParticle(_soul, _scale);
	_soul->setPosition(_feuFollet->getPositionX(), _feuFollet->getPositionY() + 30);
	this->addChild(_soul);

	// Soul
	_soulBis = CCParticleSystemQuad::create("particles/players/blue_soul.plist");
	//adjustParticle(_soulBis, _scale);
	_soulBis->setPosition(_feuFollet->getPositionX(), _feuFollet->getPositionY() - 30);
	this->addChild(_soulBis);
}

void Couche1Layer::update(float dt) 
{   
	_yAcc = GestionScene::GetInstance()->getYAcceleration();
	_xAcc = GestionScene::GetInstance()->getXAcceleration();

	_newPosition = _feuFollet->getPosition();

	#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
		windowsUpdate(dt);
	#else
		androidUpdate(dt);
	#endif

	updateSoulsPositions(dt);

	_feuFollet->setPosition(_newPosition);
	_soul->setPosition(_soulPosition);

	if (!GestionScene::GetInstance()->currentSwipe()) {
		_soulBis->setPosition(_soulPositionBis);
	}
}

void Couche1Layer::updateSoulsPositions(float dt)
{
	const float maxRadius = 65;
	const float minRadius = 40;
	const float radiusFactor = 1.5;

	static float radius = minRadius;
	static float radiusSign = 1;

	const double PI = 3.1415926535;

	if (radius >= maxRadius)
		radiusSign = -1;
	if (radius <= minRadius)
		radiusSign = 1;

	_time += dt * radiusFactor;

	radius += dt * radiusFactor * radiusSign;

	_soulPosition.x = _newPosition.x + radius * sin(_time/2*PI);
	_soulPosition.y = _newPosition.y + radius * cos(_time/2*PI);

	if (!GestionScene::GetInstance()->currentSwipe())
	{
		_soulPositionBis.x = _newPosition.x - radius * sin(_time/2*PI);
		_soulPositionBis.y = _newPosition.y - radius * cos(_time/2*PI);
	} else {
		respondToSwipe();
	}
}

void Couche1Layer::respondToSwipe()
{
	CCSwipe *s = GestionScene::GetInstance()->currentSwipe();

	if (s->points.empty()) {
		GestionScene::GetInstance()->setCurrentSwipe(0);
	} else if (!_isRespondingToSwip) {

		CCPoint tmp = _soulBis->getPosition();
		this->removeChild(_soulBis);

		_missilSoul = CCParticleSystemQuad::create("particles/missiles/blue_missile.plist");
		_missilSoul->setPosition(tmp);
		this->addChild(_missilSoul);

		swipe(_index);
	}
}

void Couche1Layer::nextSwipe()
{
	swipe(++_index);
}

void Couche1Layer::swipe(int index)
{
	CCSwipe *swipe = GestionScene::GetInstance()->currentSwipe();

	_isRespondingToSwip = true;

	if (_index >= swipe->points.size()) {
		_index = 0;
		_isRespondingToSwip = false;
		GestionScene::GetInstance()->setCurrentSwipe(0);

		CCPoint tmp = _missilSoul->getPosition();
		this->removeChild(_missilSoul);

		_soulBis = CCParticleSystemQuad::create("particles/players/blue_soul.plist");
		_soulBis->setPosition(tmp);
		this->addChild(_soulBis);
	} else {
		CCLog("Swipe index %d/%d :", _index, swipe->points.size());
		CCLog("    Go to point (%f, %f)", swipe->points[index].first.x, swipe->points[index].first.y);
		CCSequence *sequence = CCSequence::create(CCMoveTo::create(swipe->points[index].second / 500, swipe->points[index].first),
							CCCallFunc::create(this, callfunc_selector(Couche1Layer::nextSwipe)),
							NULL);
		_missilSoul->runAction(sequence);
	}
}

void Couche1Layer::androidUpdate(float dt)
{
	_newPosition.y += _yAcc * dt;
	_newPosition.x += _xAcc * dt;
}

void Couche1Layer::windowsUpdate(float dt)
{
	if (CCDirector::sharedDirector()->upArrowKeyDown)
	{
		_newPosition.y += _moveFactor;
	}

	if (CCDirector::sharedDirector()->downArrowKeyDown)
	{
		_newPosition.y -= _moveFactor;
	}

	if (CCDirector::sharedDirector()->rightArrowKeyDown)
	{
		_newPosition.x += _moveFactor;
	}
	
	if (CCDirector::sharedDirector()->leftArrowKeyDown)
	{
		_newPosition.x -= _moveFactor;
	}
}
