package com.jsoft.cocit.entityengine;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.entity.coc.ICocAction;
import com.jsoft.cocit.entity.coc.ICocEntity;
import com.jsoft.cocit.entity.coc.ICocField;
import com.jsoft.cocit.entity.coc.ICocGroup;
import com.jsoft.cocit.entityengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.entityengine.service.CocEntityService;
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
public interface EntityEngine {

	void release();

	void initEntityTypes();

	Iterator<Class> getClassOfEntityIterator();

	ICocEntity setupModuleFromDB(String tableName);

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

	List<? extends ICocEntity> getModules();

	ICocEntity getModule(Long moduleID);

	ICocEntity getModule(String moduleKey);

	List<? extends ICocGroup> getFieldGroups(ICocEntity module);

	// void validateModules() throws CocException;

	List<String> compileModule(CocEntityService module) throws CocException;

	Class getTypeOfEntity(ICocEntity module) throws CocException;

	Class getClassOfEntity(String moduleKey);

	Class getTypeOfEntity(Long moduleID);

	ICocField getFieldOfGroupBy(ICocEntity module);

	ICocField getFieldOfSelfTree(ICocEntity module);

	List<? extends ICocField> getFields(ICocEntity module);

	List<? extends ICocField> getFields(ICocGroup group);

	List<? extends ICocField> getFieldsOfEnabled(ICocEntity module);

	List<? extends ICocField> getFieldsOfEnabled(ICocGroup group);

	List<? extends ICocField> getFieldsOfGrid(ICocEntity module, String fields);

	List<? extends ICocField> getFkFields(ICocEntity module);

	List<? extends ICocField> getFkFields(ICocEntity module, Class fkType);

	List<? extends ICocField> getFieldsOfFilter(ICocEntity module);

	Map<String, ICocField> getFieldsMap(ICocEntity module);

	Map<String, ICocField> getFieldsMap(List<? extends ICocField> list);

	List<? extends ICocField> getFieldsByReferenced(ICocEntity module);

	List<? extends ICocEntity> getModulesOfSlave(ICocEntity module);

	List<? extends ICocField> getFieldsOfSlave(ICocEntity module);

	boolean isSlave(ICocEntity module);

	boolean isNumber(ICocField field);

	boolean isInteger(ICocField field);

	boolean isRichText(ICocField field);

	boolean isText(ICocField field);

	boolean isDate(ICocField field);

	boolean isString(ICocField field);

	boolean isUpload(ICocField field);

	//
	// boolean isMultiUpload(IEntityField field);

	// boolean isImage(IEntityField field);

	//
	// boolean isMultiImage(IEntityField field);

	boolean isSubModule(ICocField field);

	boolean isFakeSubSystem(ICocField field);

	boolean isBuildin(ICocEntity module, String prop);

	boolean isManyToOne(ICocField field);

	boolean isOneToOne(ICocField field);

	boolean isOneToMany(ICocField field);

	boolean isManyToMany(ICocField field);

	boolean isBoolean(ICocField field);

	boolean isEnabled(ICocField field);

	boolean isGridField(ICocField field);

	boolean isFkField(ICocField field);

	String getPropName(ICocField field);

	int getGridWidth(ICocField field);

	int getPrecision(ICocField field);

	Class getFkFieldGenericType(ICocField field) throws CocException;

	Class getTypeOfField(ICocField field) throws CocException;

	String getMode(ICocField field, ICocAction entityAction, boolean mustPriority, String defaultMode);

	String getMode(ICocGroup group, ICocAction entityAction);

	Tree makeFilterNodes(ICocEntity module, String idField, boolean removeSelfLeaf);

	Tree makeOptionNodes(ICocField field, String mode, Object data, String idField);

	List<String> makeCascadeExpr(Object obj, ICocField field, String mode);

	Option[] getOptions(ICocField field);

	String[] getCascadeMode(ICocField field, Object data);

	int getModeValue(String mode);

	Map<String, String> getMode(ICocEntity module, ICocAction entityAction, Object data);

	void validate(ICocEntity module, ICocAction entityAction, Object data, Map<String, String> fieldMode) throws CocException;

	void loadFieldValue(Object obj, ICocEntity module);

	ICocField getField(Long fieldID);

	ICocField getField(String fieldKey);

	String parseMode(String actionMode, String mode);

	String getUiMode(String mode);

	List<? extends ICocAction> getActions(ICocEntity module);

	// IEntityAction getAction(Long systemID, String opMode);

	ICocEntity getFkModule(ICocField fld);

	ICocField getFkField(ICocField fld);

	ICocGroup getFieldGroup(String key);

	String getDataTypeName(int dataType);

	IBizPlugin[] getBizPlugins(ICocAction entityAction);

	String getClassName(ICocField fld);

	String getPackageOfUDFEntity(ICocEntity module);

	String getTableName(ICocEntity module);

	String getSimpleClassName(ICocEntity module);

	String getExtendsSimpleClassName(ICocEntity module);

	Object getComplireVersion(ICocEntity module);

	String getClassName(ICocEntity module);
}
