package com.jsoft.cocit.entity.impl.coc;

import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.TreeEntity;
import com.jsoft.cocit.entity.coc.IExtCocCatalog;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "定义实体分类", key = Const.TBL_COC_CATALOG, sn = 1, dbEncoding = true, uniqueFields = Const.F_KEY, indexFields = Const.F_KEY//
           , actions = {
                   //
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v") //
           }// end actions
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "分类名称", field = "name"),//
                   @CocColumn(name = "分类编号", field = "key"),//
                   @CocColumn(name = "上级分类", field = "parentKey", fkTargetEntity = Const.TBL_COC_CATALOG, asFilterNode = true),//
                   @CocColumn(name = "序号", field = "sn"),//
           }) // end group
           }// end groups
)
public class CocCatalogImpl extends TreeEntity implements IExtCocCatalog {

}
