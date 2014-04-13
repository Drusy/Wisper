#ifndef MyCCSwipeGestureRecognizer_h
#define MyCCSwipeGestureRecognizer_h

#include "CCGestureRecognizer.h"
#include "cocos2d.h"

#define kSwipeMinDistance 1

typedef enum {
    kSwipeGestureRecognizerDirectionRight = 1 << 0,
    kSwipeGestureRecognizerDirectionLeft  = 1 << 1,
    kSwipeGestureRecognizerDirectionUp    = 1 << 2,
    kSwipeGestureRecognizerDirectionDown  = 1 << 3
} MyCCSwipeGestureRecognizerDirection;

//this class is used for storing information about the swipe gesture
class CCSwipe : public cocos2d::CCObject
{
public:
	CREATE_FUNC(CCSwipe);

    bool init() {
    	distanceTraveled = 0;

    	return true;
    }

    cocos2d::CCPoint endLocation;
    std::vector<std::pair<cocos2d::CCPoint, double> > points;
	float distanceTraveled;
};

class MyCCSwipeGestureRecognizer : public CCGestureRecognizer
{
public:
    bool init();
    ~MyCCSwipeGestureRecognizer();
    CREATE_FUNC(MyCCSwipeGestureRecognizer);
    
    virtual bool ccTouchBegan(cocos2d::CCTouch * pTouch, cocos2d::CCEvent * pEvent);
    virtual void ccTouchMoved(cocos2d::CCTouch * pTouch, cocos2d::CCEvent * pEvent);
    virtual void ccTouchEnded(cocos2d::CCTouch * pTouch, cocos2d::CCEvent * pEvent);
protected:
    CC_SYNTHESIZE(int, direction, Direction);
private:
    cocos2d::cc_timeval startTime;
	cocos2d::cc_timeval endTime;
	bool isPress;
    cocos2d::CCPoint startLocation;
    CCSwipe *_currentSwipe;
    
    bool checkSwipeDirection(cocos2d::CCPoint p1, cocos2d::CCPoint p2, int & dir);
};

#endif
