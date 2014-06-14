#include "MobileTouchGestion.h"
#include "View/Gestion/GestionScene.h"
#include "View/CustomClasses/CCGestureRecognizer/CCTapGestureRecognizer.h"
#include "View/CustomClasses/CCGestureRecognizer/MyCCSwipeGestureRecognizer.h"

using namespace cocos2d;

// on "init" you need to initialize your instance
bool MobileTouchGestion::init()
{
	/// Appel de la classe parent.
    if ( !CCLayer::init() )
    {
        return false;
    }

    /// On active l'accelerometre et le click pour cette scene.
    this->setAccelerometerEnabled(true);
    this->setTouchEnabled(true);
	this->setKeypadEnabled(true);

	// Default accelerometer calibration
	_calX = 0;
	_calY = 0;

    return true;
}

void MobileTouchGestion::calibrateAccelerometer(CCAcceleration* pAccelerationValue)
{
	_calX = pAccelerationValue->x;
	_calY = pAccelerationValue->y;
	_calZ = pAccelerationValue->z;

	CCLog("Accelerometer calibrated : (x : %f, y : %f, z : %f)", _calX, _calY, _calZ);
}

/**
 * Cocos2DX inverts X and Y accelerometer depending on device orientation
 * in landscape mode right x=-y and y=x !!! (Strange and confusing choice)
 */
void MobileTouchGestion::didAccelerate(CCAcceleration* pAccelerationValue)
{    

	static bool calibrated = false;
	const double epsilon = 0.02;

	// Calibrate
	if (!calibrated)
	{
		calibrateAccelerometer(pAccelerationValue);
		calibrated = true;
	}

    // Get window size
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();

    #define MAX_DPL_PAR_SEC (winSize.height * 0.6f) /// Window ratio
    #define SPEED_FACTOR 2.0f /// Speed

    double yOffset = pAccelerationValue->y - _calY;
    double xOffset = pAccelerationValue->x - _calX;
    double zOffset = pAccelerationValue->z - _calZ;

    double yValue = (yOffset - zOffset) / 2;
    double xValue = (xOffset - zOffset) / 2;

    // Y
    double yValue_abs = yValue;
    // Absolute offset value
    if (yValue_abs < 0)
    	yValue_abs *= -1;

    if (yValue_abs <= epsilon)
        yValue = 0;

    // X
    double xValue_abs = xValue;
	// Absolute offset value
	if (xValue_abs < 0)
		xValue_abs *= -1;

	if (xValue_abs <= epsilon)
		xValue = 0;

    double yAccelerationPerSec = MAX_DPL_PAR_SEC * yValue * SPEED_FACTOR;
    double xAccelerationPerSec = MAX_DPL_PAR_SEC * xValue * SPEED_FACTOR;

    GestionScene::GetInstance()->setYAcceleration(yAccelerationPerSec);
    GestionScene::GetInstance()->setXAcceleration(xAccelerationPerSec);
}

void MobileTouchGestion::didSingleTap(CCObject *sender)
{
}

void MobileTouchGestion::didDoubleTap(CCObject *sender)
{
}

void MobileTouchGestion::didSwipe(CCObject *sender)
{
	CCSwipe *swipe = (CCSwipe*)sender;
	if (!GestionScene::GetInstance()->currentSwipe()) {

		CCLog("didSwipe -> Distance : %f", swipe->distanceTraveled);
		CCLog("didSwipe -> Points recorded : %d", swipe->points.size());

		GestionScene::GetInstance()->setCurrentSwipe(swipe);
	} else {
		CCLog("Swipe ignored");
		swipe->release();
	}
}

void MobileTouchGestion::keyBackClicked()
{
	endScene();
}

void MobileTouchGestion::endScene()
{
	CCDirector::sharedDirector()->end();
	//GestionScene::GetInstance()->changeScene(1);
}
