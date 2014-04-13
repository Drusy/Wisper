#ifndef __MobileTouchGestion_H__
#define __MobileTouchGestion_H__

#include "cocos2d.h"
//#include "View/CustomClasses/CCLayerWithTabAdvancedGesture.h"

USING_NS_CC;

class AppDelegate;

class MobileTouchGestion : public CCLayer
{
private:
	/// X calibration of the accelerometer
	double _calX;
	/// Y calibration of the accelerometer
	double _calY;
	/// Z calibration of the accelerometer
	double _calZ;

public:
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // implement the "static node()" method manually
    CREATE_FUNC(MobileTouchGestion);
	
	/// Accelerometer.
    virtual void didAccelerate(CCAcceleration* pAccelerationValue);
	/// Click
	void didSingleTap(CCObject *sender);
	/// Double click
	void didDoubleTap(CCObject *sender);
	/// Swipe
	void didSwipe(CCObject *sender);
	/// Touche retourne pressé sous Android
	virtual void keyBackClicked();
    /// Méthode appelé à la fin de la séquence.
    void endScene();
    // Calibrate accelerometer
    void calibrateAccelerometer(CCAcceleration* pAccelerationValue);
};

#endif // __MobileTouchGestion_H__
