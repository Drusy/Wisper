#ifndef __header__Strategy__
#define __header__Strategy__

#include <string>

class Item;

/**
 * @enum StrategyEnum
 *
 * Defines a strategy
 */
enum StrategyEnum
{
};

/**
 * @class Strategy
 *
 * Virtual class defines a strategy
 */
class Strategy
{
public:
    Strategy();
    virtual ~Strategy();

    virtual void execute(Item *item) = 0;
    virtual std::string toString() = 0;
    
    static unsigned count();
    
protected:
    static unsigned _count;
};

#endif
