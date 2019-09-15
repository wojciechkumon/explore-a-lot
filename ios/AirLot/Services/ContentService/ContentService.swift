//
//  ContentService.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import Foundation

class ContentService {
    static let shared = ContentService()
    private init() {}
    
    var startDate: Date!
    var finishDate: Date!
    var numberOfDays: Int!
    var numberOfAdults: Int = 0
    var numberOfTeenagers: Int = 0
    var numberOfChildren: Int = 0
    var numberOfInfants: Int = 0
    var tags: [Tags]!
    var maxPrice: Decimal!
    var origin: String = "WAW"
}
