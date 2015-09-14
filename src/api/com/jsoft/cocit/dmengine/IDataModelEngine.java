package com.jsoft.cocit.dmengine;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.coc.ICocActionEntity;
import com.jsoft.cocit.baseentity.coc.ICocEntity;
import com.jsoft.cocit.baseentity.coc.ICocFieldEntity;
import com.jsoft.cocit.baseentity.coc.ICocGroupEntity;
import com.jsoft.cocit.dmengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.Tree;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IDataModelEngine {

	void release();

	void initEntityTypes();

	Iterator<Class> getClassOfEntityIterator();

	ICocEntity setupModuleFromDB(String tableName);

	void setupCocitDBTables();

	void setupCocitFromPackage();

	void setupCocitFromXls(File excelFile);

	// List<IEntityModule> setupModulesFromPackage(Orm orm) throws CocException;

	// IEntityModule setupModuleFromClass(Orm orm, Class classOfEntity) throws CocException;

	// List setupEntityDatasFromPackage(Orm orm, String tenantKey);

	// <T> List<T> setupEntityDataFromClass(Orm orm, String tenantKey, Class<?> classOfEntity);

	// <T> List<T> setupEntityDataFromJson(Orm orm, String tenantKey, Class<T> classOfEntity, String json);

	// void parseModuleByAnnotation(Orm orm, Class classOfEntity, IEntityModule toModule);

	String getExtendsClassName(ICocEntity module);

	int importFromJson(String tenantKey, String folder);

	int exportToJson(String tenantKey, String folder, CndExpr expr) throws IOException;

	int exportToJson(String tenantKey, String folder, CndExpr expr, ICocEntityInfo sys) throws IOException;

	List<? extends ICocEntity> getModules();

	ICocEntity getModule(Long moduleID);

	ICocEntity getModule(String moduleKey);

	List<? extends ICocGroupEntity> getFieldGroups(ICocEntity module);

	// void validateModules() throws CocException;

	List<String> compileModule(ICocEntityInfo module) throws CocException;

	Class getTypeOfEntity(ICocEntity module) throws CocException;

	Class getClassOfEntity(String moduleKey);

	Class getTypeOfEntity(Long moduleID);

	ICocFieldEntity getFieldOfGroupBy(ICocEntity module);

	ICocFieldEntity getFieldOfSelfTree(ICocEntity module);

	List<? extends ICocFieldEntity> getFields(ICocEntity module);

	List<? extends ICocFieldEntity> getFields(ICocGroupEntity group);

	List<? extends ICocFieldEntity> getFieldsOfEnabled(ICocEntity module);

	List<? extends ICocFieldEntity> getFieldsOfEnabled(ICocGroupEntity group);

	List<? extends ICocFieldEntity> getFieldsOfGrid(ICocEntity module, String fields);

	List<? extends ICocFieldEntity> getFkFields(ICocEntity module);

	List<? extends ICocFieldEntity> getFkFields(ICocEntity module, Class fkType);

	List<? extends ICocFieldEntity> getFieldsOfFilter(ICocEntity module);

	Map<String, ICocFieldEntity> getFieldsMap(ICocEntity module);

	Map<String, ICocFieldEntity> getFieldsMap(List<? extends ICocFieldEntity> list);

	List<? extends ICocFieldEntity> getFieldsByReferenced(ICocEntity module);

	List<? extends ICocEntity> getModulesOfSlave(ICocEntity module);

	List<? extends ICocFieldEntity> getFieldsOfSlave(ICocEntity module);

	boolean isSlave(ICocEntity module);

	boolean isNumber(ICocFieldEntity field);

	boolean isInteger(ICocFieldEntity field);

	boolean isRichText(ICocFieldEntity field);

	boolean isText(ICocFieldEntity field);

	boolean isDate(ICocFieldEntity field);

	boolean isString(ICocFieldEntity field);

	boolean isUpload(ICocFieldEntity field);

	//
	// boolean isMultiUpload(IEntityField field);

	// boolean isImage(IEntityField field);

	//
	// boolean isMultiImage(IEntityField field);

	boolean isSubModule(ICocFieldEntity field);

	boolean isFakeSubSystem(ICocFieldEntity field);

	boolean isBuildin(ICocEntity module, String prop);

	boolean isManyToOne(ICocFieldEntity field);

	boolean isOneToOne(ICocFieldEntity field);

	boolean isOneToMany(ICocFieldEntity field);

	boolean isManyToMany(ICocFieldEntity field);

	boolean isBoolean(ICocFieldEntity field);

	boolean isEnabled(ICocFieldEntity field);

	boolean isGridField(ICocFieldEntity field);

	boolean isFkField(ICocFieldEntity field);

	String getPropName(ICocFieldEntity field);

	int getGridWidth(ICocFieldEntity field);

	int getPrecision(ICocFieldEntity field);

	Class getFkFieldGenericType(ICocFieldEntity field) throws CocException;

	Class getTypeOfField(ICocFieldEntity field) throws CocException;

	String getMode(ICocFieldEntity field, ICocActionEntity entityAction, boolean mustPriority, String defaultMode);

	String getMode(ICocGroupEntity group, ICocActionEntity entityAction);

	Tree makeFilterNodes(ICocEntity module, String idField, boolean removeSelfLeaf);

	Tree makeOptionNodes(ICocFieldEntity field, String mode, Object data, String idField);

	List<String> makeCascadeExpr(Object obj, ICocFieldEntity field, String mode);

	Option[] getOptions(ICocFieldEntity field);

	String[] getCascadeMode(ICocFieldEntity field, Object data);

	int getModeValue(String mode);

	Map<String, String> getMode(ICocEntity module, ICocActionEntity entityAction, Object data);

	void validate(ICocEntity module, ICocActionEntity entityAction, Object data, Map<String, String> fieldMode) throws CocException;

	void loadFieldValue(Object obj, ICocEntity module);

	ICocFieldEntity getField(Long fieldID);

	ICocFieldEntity getField(String fieldKey);

	String parseMode(String actionMode, String mode);

	String getUiMode(String mode);

	List<? extends ICocActionEntity> getActions(ICocEntity module);

	// IEntityAction getAction(Long systemID, String opMode);

	ICocEntity getFkModule(ICocFieldEntity fld);

	ICocFieldEntity getFkField(ICocFieldEntity fld);

	ICocGroupEntity getFieldGroup(String key);

	String getDataTypeName(int dataType);

	IBizPlugin[] getBizPlugins(ICocActionEntity entityAction);

	String getClassName(ICocFieldEntity fld);

	String getPackageOfUDFEntity(ICocEntity module);

	String getTableName(ICocEntity module);

	String getSimpleClassName(ICocEntity module);

	String getExtendsSimpleClassName(ICocEntity module);

	Object getComplireVersion(ICocEntity module);

	String getClassName(ICocEntity module);
}
