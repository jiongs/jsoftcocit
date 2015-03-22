package com.jsoft.cocimpl.action;

import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.ChainBy;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.LoadingBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.UrlMappingBy;
import org.nutz.mvc.annotation.Views;

import com.jsoft.cocimpl.mvc.nutz.CocEntityParamAdaptor;
import com.jsoft.cocimpl.mvc.nutz.CocLoading;
import com.jsoft.cocimpl.mvc.nutz.CocSetup;
import com.jsoft.cocimpl.mvc.nutz.CocUrlMappingImpl;
import com.jsoft.cocimpl.mvc.nutz.CocViewMaker;

@Encoding(input = "utf-8", output = "utf-8")
@Fail("json")
@SetupBy(CocSetup.class)
@Filters(//
{ // @By(type = ActionMappingFilter.class)
}//
)
@AdaptBy(//
type = CocEntityParamAdaptor.class//
)
@Views(//
value = { CocViewMaker.class })
// @IocBy(type = CocIocProvider.class, args = { "" })
@LoadingBy(CocLoading.class)
@Modules(//
scanPackage = true//
)
@UrlMappingBy(CocUrlMappingImpl.class)
@ChainBy(args = { "com/jsoft/cocit/config/coc-chains.js" })
@Chain(value = "Cocit")
public interface MainModule {
}
