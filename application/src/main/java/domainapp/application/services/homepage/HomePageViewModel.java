/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.application.services.homepage;

import java.util.List;

import org.apache.isis.applib.annotation.ViewModel;
import org.apache.isis.applib.services.i18n.TranslatableString;

import domainapp.modules.cmn.dom.impl.Profile;
import domainapp.modules.cmn.dom.impl.ProfileRepository;
import domainapp.modules.frd.dom.impl.AssetGroup;
import domainapp.modules.frd.dom.impl.AssetGroupRepository;
import domainapp.modules.frd.dom.impl.AssetType;
import domainapp.modules.frd.dom.impl.AssetTypeRepository;
import domainapp.modules.frd.dom.impl.ContractUnit;
import domainapp.modules.frd.dom.impl.ContractUnitRepository;
import domainapp.modules.frd.dom.impl.RuleChapter;
import domainapp.modules.frd.dom.impl.RuleChapterRepository;
import domainapp.modules.frd.dom.impl.SubAssetType;
import domainapp.modules.frd.dom.impl.SubAssetTypeRepository;

@ViewModel
public class HomePageViewModel {

    public TranslatableString title() {
        return TranslatableString.tr("{num} profiles", "num", getProfiles().size());
    }

    public List<Profile> getProfiles() {
        return profileRepository.listAll();
    }

    @javax.inject.Inject
    ProfileRepository profileRepository;

    public List<AssetGroup> getAssetGroups() {
        return assetGroupRepository.listAll();
    }

    @javax.inject.Inject
    AssetGroupRepository assetGroupRepository;

    public List<AssetType> getAssetTypes() {
        return assetTypeRepository.listAll();
    }

    @javax.inject.Inject
    AssetTypeRepository assetTypeRepository;

    public List<SubAssetType> getSubAssetTypes() {
        return subAssetTypeRepository.listAll();
    }

    @javax.inject.Inject
    SubAssetTypeRepository subAssetTypeRepository;

    public List<RuleChapter> getRuleChapters() {
        return ruleChapterRepository.listAll();
    }

    @javax.inject.Inject
    RuleChapterRepository ruleChapterRepository;

    public List<ContractUnit> getContractUnits() {
        return contractUnitRepository.listAll();
    }

    @javax.inject.Inject
    ContractUnitRepository contractUnitRepository;
}
