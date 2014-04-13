#include "Strategy.h"
#include "Item.h"

unsigned Strategy::_count = 0;

Strategy::Strategy()
{
}

Strategy::~Strategy()
{
}

unsigned Strategy::count()
{
    return _count;
}
