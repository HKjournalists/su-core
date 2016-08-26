package com.lhfs.fsn.cache;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhCacheFactory {
	private Cache cache;
	private CacheManager manager;
	private static EhCacheFactory instance;
	private static Logger log = Logger.getLogger( EhCacheFactory.class);
	
	public static EhCacheFactory getInstance(){
		try {
			if(instance == null || instance.getCache()==null || instance.getManager()==null){
				instance = init();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return instance;
	}
	public Cache getCache() {
		return cache;
	}
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	public CacheManager getManager() {
		return manager;
	}
	public void setManager(CacheManager manager) {
		this.manager = manager;
	}
	public static EhCacheFactory init() throws FileNotFoundException {
		log.debug("[EhcacheUtil.init]");
		System.setProperty( "net.sf.ehcache.enableShutdownHook", "true" ) ;
		if (instance == null) {
			instance = new EhCacheFactory();
		}
		CacheManager manage = CacheManager.create(EhCacheFactory.class.getResourceAsStream("ehcache.xml"));
		instance.setManager(manage);
		manage.addCache("mycache");
		Cache cach = manage.getCache("mycache");
		instance.setCache(cach);
		return instance;
	}
	private static boolean isNull( Element e){
		return e==null || e.getObjectValue()==null || e.getValue()==null;
	}
	/**
	 * 存入
	 * @param <T>
	 * @param cache 缓存库
	 * @param key 键
	 * @param value 值
	 */
	public static <T extends Serializable> void put(String key, T value) {
		Element e = new Element( key, value) ;
		getInstance().getCache().put(e) ;
//		getInstance().getCache().flush() ;
	}
	/**
	 * 存入 并设置元素是否永恒保存
	 * @param <T>
	 * @param cache 缓存库
	 * @param key 键
	 * @param value 值
	 */
	public static <T extends Serializable> void put(String key, T value, boolean eternal) {
		Element element = new Element( key, value) ;
		element.setEternal( eternal) ;
		getInstance().getCache().put(element) ;
//		getInstance().getCache().flush() ;
	}
	/**
	 * 存入
	 * @param <T>
	 * @param cache 缓存库
	 * @param key 键
	 * @param value 值
	 * @param timeToLiveSeconds 最大存活时间
	 * @param timeToIdleSeconds 最大访问间隔时间
	 */
	public static <T extends Serializable> void put(String key, T value, int timeToLiveSeconds, int timeToIdleSeconds) {
		Element element = new Element( key, value) ;
		element.setTimeToLive( timeToLiveSeconds) ;
		element.setTimeToIdle( timeToIdleSeconds) ;
		getInstance().getCache().put(element);
//		getInstance().getCache().flush( );
	}
	public static Object getCacheElement(String key) {
		Element e=getInstance().getCache().get(key) ;
		return e;
	}
	public static Object get(String key) {
		Element e=getInstance().getCache().get( key) ;
		if( e!=null){
			return e.getObjectValue( ) ;
		}
		return null;
	}
	public static void remove(String key) {
		getInstance().getCache().remove( key) ;
	}

	@SuppressWarnings("unchecked")
	public static void addToList(String key, Serializable value) {
		Element e = getInstance().getCache().get( key) ;
		if ( isNull( e) ) {
			List<Serializable> list = Collections.synchronizedList( new LinkedList<Serializable>( ) ) ;
			list.add( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		} else {
			List<Serializable> list = ( List<Serializable>) e.getObjectValue( ) ;
			list.add( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		}
//		getInstance().getCache().flush( ) ;
	}
	@SuppressWarnings("unchecked")
	public static void addAllToList(String key, Collection<? extends Serializable> value) {
		Element e = getInstance().getCache().get( key) ;
		if ( isNull( e) ) {
			List<Serializable> list = Collections.synchronizedList( new LinkedList<Serializable>( ) ) ;
			list.addAll( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		} else {
			List<Serializable> list = ( List<Serializable>) e.getObjectValue( ) ;
			list.addAll( value) ;
			log.debug( key+" - - " +list) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		}
//		getInstance().getCache().flush( ) ;
	}
	@SuppressWarnings("unchecked")
	public static void addToHashSet(String key, Serializable value) {
		Element e = getInstance().getCache().get( key) ;
		if ( isNull( e) ) {
			Set<Serializable> list = Collections.synchronizedSet( new HashSet<Serializable>( ) ) ;
			list. add( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		} else {
			Set<Serializable> list = ( Set<Serializable>)e.getObjectValue( ) ;
			list.add(value);
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		}
//		getInstance().getCache().flush( ) ;
	}
	@SuppressWarnings("unchecked")
	public static void addAllToHashSet(String key, Collection<? extends Serializable> value) {
		Element e = getInstance().getCache().get( key) ;
		if ( isNull( e) ) {
			Set<Serializable> list = Collections.synchronizedSet( new HashSet<Serializable>( ) ) ;
			list.addAll( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		} else {
			Set<Serializable> list = ( Set<Serializable>) e.getObjectValue( ) ;
			list. addAll( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		}
//		getInstance().getCache().flush( ) ;
	}
	@SuppressWarnings("unchecked")
	public static void addToArrayList( String key, Serializable value) {
		Element e = getInstance().getCache().get( key) ;
		if ( isNull( e) ) {
			List<Serializable> list = Collections.synchronizedList( new ArrayList<Serializable>( ) ) ;
			list.add(value);
			e = new Element( key, list) ;
			e. setEternal( true) ;
			getInstance().getCache().put(e) ;
		} else {
			List<Serializable> list = ( List<Serializable>) e.getObjectValue( ) ;
			list.add(value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		}
//		getInstance().getCache().flush( ) ;
	}
	@SuppressWarnings("unchecked")
	public static void addAllToArrayList( String key, Collection<? extends Serializable> value) {
		Element e = getInstance().getCache().get( key) ;
		if ( isNull( e) ) {
			List<Serializable> list = Collections. synchronizedList( new ArrayList<Serializable>( ) ) ;
			list.addAll( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		} else {
			List<Serializable> list = ( List<Serializable>) e.getObjectValue( ) ;
			list.addAll( value) ;
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
		}
//		getInstance().getCache().flush( ) ;
	}
	@SuppressWarnings( "unchecked" )
	public static <T extends Serializable> T popFromList(String key, Class<T> T){
		Element e=getInstance().getCache().get( key) ;
		if( e!=null){
			List<Serializable> list=( List<Serializable>) e.getObjectValue( ) ;
			Iterator<Serializable> it=list.iterator( ) ;
			if( list.size() >0){
				Serializable obj =it.next( ) ;
				it.remove( ) ;
				e=new Element( key, list) ;
				e.setEternal( true) ;
				getInstance().getCache().put( e) ;
//				getInstance().getCache().flush( ) ;
				return ( T) obj ;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> List<T> popFromList(String key,
			int count, Class<T> T) {
		Element e = getInstance().getCache().get( key) ;
		if ( e != null) {
			List<Serializable> list = ( List<Serializable>) e.getObjectValue( ) ;
			if( count<1){
				List<T> result = ( List<T>) new ArrayList<Serializable>( list) ;
				list.clear( ) ;
				e = new Element( key, list) ;
				e.setEternal( true) ;
				getInstance().getCache().put( e) ;
//				getInstance().getCache().flush( ) ;
				return result;
			}
			List<T> result = new ArrayList<T>( count) ;
			Iterator<Serializable> it=list. iterator( ) ;
			for ( int i = 0; i < count && it. hasNext( ) ; i++) {
				Serializable obj = it.next( ) ;
				it.remove( ) ;
				result.add( ( T) obj ) ;
			}
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
//			getInstance().getCache().flush( ) ;
			return result;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T popFromHashSet(String key, Class<T> T){
		Element e=getInstance().getCache().get( key) ;
		if( e!=null){
			Set<Serializable> list=( Set<Serializable>) e.getObjectValue( ) ;
			Iterator<Serializable> it=list.iterator( ) ;
			if( list. size( ) >0){
				Serializable obj =it.next( ) ;
				it.remove( ) ;
				e=new Element( key, list) ;
				e.setEternal( true) ;
				getInstance().getCache().put( e) ;
//				getInstance().getCache().flush( ) ;
				return ( T) obj ;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> List<T> popFromHashSet(String key,
			int count, Class<T> T) {
		Element e = getInstance().getCache().get( key) ;
		if ( e != null) {
			Set<Serializable> list = ( Set<Serializable>) e.getObjectValue( ) ;
			if( count<1){
				List<T> result = ( List<T>) new ArrayList<Serializable>( list) ;
				list. clear( ) ;
				e = new Element( key, list) ;
				e.setEternal( true) ;
				getInstance().getCache().put( e) ;
//				getInstance().getCache().flush( ) ;
				return result;
			}
			List<T> result = new ArrayList<T>( count) ;
			Iterator<Serializable> it=list.iterator( ) ;
			for ( int i = 0; i < count && it.hasNext( ) ; i++) {
				Serializable obj = it.next( ) ;
				it.remove( ) ;
				result. add( ( T) obj ) ;
			}
			e = new Element( key, list) ;
			e.setEternal( true) ;
			getInstance().getCache().put( e) ;
//			getInstance().getCache().flush( ) ;
			return result;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static int getCollectionSize(String key) {
		Element e = getInstance().getCache().get( key) ;
		if ( e != null) {
			Collection<Serializable> list = ( Collection<Serializable>) e.getObjectValue( ) ;
			return list.size( ) ;
		}
		return 0;
	}
	@SuppressWarnings("rawtypes")
	public static List getKeys( Cache cache) {
		return cache.getKeys( ) ;
	}
	public static List<String> getKeys(String start) {
		List<?> list=getInstance().getCache().getKeys( ) ;
		List<String> result=new ArrayList<String>( list.size( ) ) ;
		for( Object obj : list){
			if( obj !=null&&obj.getClass( ) ==String.class){
				String s=( String) obj ;
				if( s.startsWith( start) )
					result.add( s) ;
			}
		}
		return result;
	}
	/*public static void main( String[ ] args){
		put( httpStatusCache, "xiaochen" , "a" ) ;
		List<String> list = new ArrayList<String>( ) ;
		list.add( "b" ) ;
		list.add( "c" ) ;
		list.add( "d" ) ;
		put( httpStatusCache, "xiaochen" , "b" ) ;
		put( httpStatusCache, "xiaochen" , "c" ) ;
		Element aaaa = new Element( "xiaohan" , list) ;
		httpStatusCache.put( aaaa) ;
		Element element = httpStatusCache.get( "xiaochen" ) ;
		Element aaaaa = httpStatusCache.get( "xiaohan" ) ;
		System.out.println( " aaaa : " + aaaaa.getValue( ) ) ;
		System.out.println( " ** : " + element.getValue( ) ) ;
		System.out.println( " keys : " + httpStatusCache.getKeys( ).size( ) ) ;
		System.out.println( " keys : " + httpStatusCache.getKeys( ).get( 1) ) ;
		int a = 10 ;
		assert a==10 : " Out assertion failed! " ;
		element. getValue( ) ;
	}*/
}
