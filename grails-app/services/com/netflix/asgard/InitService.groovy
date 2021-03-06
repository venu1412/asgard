/*
 * Copyright 2012 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.asgard

import java.awt.TexturePaintContext.Int;
import java.awt.event.ItemEvent;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import com.netflix.asgard.cache.CacheInitializer
import com.netflix.asgard.cache.Fillable
import com.sun.org.apache.bcel.internal.generic.NEW;

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class InitService implements ApplicationContextAware {

	static transactional = false

	ApplicationContext applicationContext
	Caches caches

	def configService
	def regionService
	def grailsApplication // modifying the config object directly here
	CachedMapBuilder cachedMapBuilder

	/**
	 * Creates the Asgard Config.groovy file and updates the in memory configuration to reflect the configured state
	 *
	 * @param configObject The configuration to persist
	 */
	void writeConfig(ConfigObject configObject) throws IOException {
		File asgardHomeDir = new File(configService.asgardHome)
		asgardHomeDir.mkdirs()
		if (!asgardHomeDir.exists()) {
			throw new IOException("Unable to create directory ${configService.asgardHome}")
		}

		File configFile = new File(configService.asgardHome, 'Config.groovy')
		if(!configFile.exists()) {
			boolean fileCreated = configFile.createNewFile()
			if (!fileCreated && !configFile.exists()) {
				throw new IOException("Unable to create Config.groovy file in directory ${configService.asgardHome}")
			}
		}

		ConfigObject config = new ConfigSlurper().parse(configFile.toURL());
		config.merge(configObject)

		configFile.withWriter{ writer ->
			config.writeTo(writer)
		}

		grailsApplication.config.appConfigured = true
		grailsApplication.config.merge(config)

		initializeApplication()
	}

	/**
	 * Kicks off populating of caches and background threads
	 */
	void initializeApplication() {

		removeCaches()
		log.info 'Starting caches'
		Collection<CacheInitializer> cacheInitializers = applicationContext.getBeansOfType(CacheInitializer).values()
		for (CacheInitializer cacheInitializer in cacheInitializers) {
			cacheInitializer.initializeCaches()
		}
		/*log.info 'Starting background threads'
		Collection<BackgroundProcessInitializer> backgroundProcessInitializers =
				applicationContext.getBeansOfType(BackgroundProcessInitializer).values()
		for (BackgroundProcessInitializer backgroundProcessInitializer in backgroundProcessInitializers) {
			try{
				backgroundProcessInitializer.cancel()
			
			}catch(Exception e){
			
				log.info "error :"+e.getMessage()
				log.error "error while Stopping the back ground thread, "
				

			}
			backgroundProcessInitializer.initializeBackgroundProcess()
		}*/
	}

	/**
	 * @return true if all caches have completed their initial load, false otherwise
	 */
	boolean cachesFilled() {
		Collection<Fillable> fillableCaches = [caches.allImages,caches.allInstances,caches.allVolumes,caches.allSecurityGroups]
		!fillableCaches.find {
			!it.filled
			 }
		
		
	}
	void removeCaches() {
		regionService.reloadRegions = true
		cachedMapBuilder.regions = regionService.values()
		caches.rebuild(cachedMapBuilder,configService)

	}
}
