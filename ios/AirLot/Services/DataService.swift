//
//  DataService.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 14/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import Foundation

class DataService {
    
    static func content() -> [CardModel] {
        var content: [CardModel] = []
        var x = Tags.allCases.shuffled()
        for item in x[0..<6] {
            content.append(CardModel(tag: item))
        }
        return content
    }
}
