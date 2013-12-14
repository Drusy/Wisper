#include "MyCCSwipeGestureRecognizer.h"
#include "cocos2d.h"
#include <iostream>

USING_NS_CC;

bool MyCCSwipeGestureRecognizer::init()
{
	isPress = false;
    direction = 0;

    return true;
}

MyCCSwipeGestureRecognizer::~MyCCSwipeGestureRecognizer()
{
    
}

bool MyCCSwipeGestureRecognizer::checkSwipeDirection(CCPoint p1, CCPoint p2, int & dir)
{
    bool right = p2.x - p1.x >= kSwipeMinDistance;
    bool left = p1.x - p2.x >= kSwipeMinDistance;
    bool down = p1.y - p2.y >= kSwipeMinDistance;
    bool up = p2.y - p1.y >= kSwipeMinDistance;
    
    if (up) {
        if ((direction & kSwipeGestureRecognizerDirectionUp) == kSwipeGestureRecognizerDirectionUp) {
            dir = kSwipeGestureRecognizerDirectionUp;
            return true;
        }
    }
    else if (down) {
        if ((direction & kSwipeGestureRecognizerDirectionDown) == kSwipeGestureRecognizerDirectionDown) {
            dir = kSwipeGestureRecognizerDirectionDown;
            return true;
        }
    }
    else if (right) {
        if ((direction & kSwipeGestureRecognizerDirectionRight) == kSwipeGestureRecognizerDirectionRight) {
            dir = kSwipeGestureRecognizerDirectionRight;
            return true;
        }
    }
    else if (left) {
        if ((direction & kSwipeGestureRecognizerDirectionLeft) == kSwipeGestureRecognizerDirectionLeft) {
            dir = kSwipeGestureRecognizerDirectionLeft;
            return true;
        }
    }
    
    return false;
}

bool MyCCSwipeGestureRecognizer::ccTouchBegan(CCTouch * pTouch, CCEvent * pEvent)
{
    if (isRecognizing) {
        return false;
    }

    startLocation = pTouch->getLocation();
	if (!isPositionBetweenBounds(startLocation)) return false;

	_currentSwipe = CCSwipe::create();
	_currentSwipe->retain();

	CCTime::gettimeofdayCocos2d(&startTime, NULL);
    
    isRecognizing = true;
	isPress = true;
    return true;
}

void MyCCSwipeGestureRecognizer::ccTouchMoved(cocos2d::CCTouch * pTouch, cocos2d::CCEvent * pEvent)
{
	if(isPress)
	{
		CCPoint position = pTouch->getLocation();
    
		//distance between initial and final point of touch
		float distance = distanceBetweenPoints(startLocation, position);

		CCTime::gettimeofdayCocos2d(&endTime, NULL);
		double time = cocos2d::CCTime::timersubCocos2d(&startTime, &endTime);
		startTime = endTime;

		if (distance >= kSwipeMinDistance) {
			_currentSwipe->points.push_back(std::pair<cocos2d::CCPoint, double>(position, time));
			_currentSwipe->distanceTraveled += distance;

			if (cancelsTouchesInView)
				stopTouchesPropagation(createSetWithTouch(pTouch), pEvent); //cancel touch over other views
		}
	}
}

void MyCCSwipeGestureRecognizer::ccTouchEnded(CCTouch * pTouch, CCEvent * pEvent)
{
	CCPoint position = pTouch->getLocation();

	_currentSwipe->endLocation = position;

	gestureRecognized(_currentSwipe);

	isPress = false;
	isRecognizing = false;
}
