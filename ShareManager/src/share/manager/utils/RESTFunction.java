package share.manager.utils;

public enum RESTFunction {
	GET_COMPANY_STOCK(1),
	GET_COMPANY_RANGE_STOCK(2),	
	GET_COMPANY_STOCK_PORTFOLIO(3),
	GET_COMPANY_RANGE_STOCK_PORTFOLIO(4),
	GET_COMPANY_NAMES(5),
	VALIDITY_CHECK(6),
	NONE(-1);
	
    private final int what;       

    private RESTFunction(int s) {
        what = s;
    }

    public boolean equalsName(int otherWhat){
        return (otherWhat == -1) ? false: what == otherWhat;
    }

    public int toInt(){
       return what;
    }
}
