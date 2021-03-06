#include "MenuLayer.h"
#include "View/Gestion/GestionScene.h"

using namespace cocos2d;

// on "init" you need to initialize your instance
bool MenuLayer::init()
{
	/// Appel de la classe parent.
    if ( !CCLayer::init() )
    {
        return false;
    }

    /// On d�sactive l'accelerometre et la click pour cette scene.
    this->setAccelerometerEnabled(false);
    this->setTouchEnabled(false);

    /// On r�cup�re la taille de l'�cran et calcul du rapport.
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	_scale = winSize.height/720.0f;

	/// Cr�ation d'un label � afficher.
	_labelName = CCLabelTTF::create("MenuLayer", "Arial", 60 * _scale);

	/// On ajoute les images au layer et on les masque.
	this->addChild(_labelName, 0);

	/// On cr�� et on ajoute les s�quences d'animations pour les deux images.	
	_labelName->runAction(CCSequence::create(CCFadeOut::create(0.0f), /// On le fait disparaitre
											CCMoveTo::create(0,ccp(winSize.width * 0.5f, winSize.height * 0.5f)), /// On le place ou il faut.
											CCDelayTime::create(0.5f), /// On attend une demi seconde.
											CCFadeIn::create(2.0f), /// On le faut apparaitre en 2 secondes.
											CCCallFunc::create(this, callfunc_selector(MenuLayer::endScene)), /// On appel la fonction pour mettre un terme � la sc�ne.
											NULL));

    return true;
}

void MenuLayer::endScene()
{
	GestionScene::GetInstance()->changeScene(1);
}
