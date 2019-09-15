//
//  UrlHelper.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import Foundation

struct UrlHelper {
    
    static func url(for model: ContentService) -> String {
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let departureDateStart: String = dateFormatter.string(from: model.startDate)
        let departureDateFinish: String = dateFormatter.string(from: model.finishDate)
        let decimalToDouble = Double(truncating: NSDecimalNumber(decimal: model.maxPrice))
        let numberOfDays: Int = model.numberOfDays
        var tags: String = ""
        
        for item in model.tags {
            tags += item.rawValue + ","
        }
        
        if !tags.isEmpty {
            tags = String(tags.prefix(tags.count - 1))
        }
        
       return "http://ec2-54-93-38-93.eu-central-1.compute.amazonaws" + ".com:8080/api/lot/tags/flights?origin=WAW&numberOfAdults="+"\(model.numberOfAdults)"+"&numberOfTeenagers="+"\(model.numberOfTeenagers)&numberOfInfants="+"\(model.numberOfInfants)&numberOfChildren="+"\(model.numberOfChildren)&departureDateStart="+"\(departureDateStart)"+"&departureDateEnd="+"\(departureDateFinish)&tripDurationDays="+"\(numberOfDays)&maxPricePerPerson="+"\(decimalToDouble)&tags=\(tags)"
    }
    
    static func luckyUrl(for model: ContentService) -> String {
        return "http://ec2-54-93-38-93.eu-central-1.compute.amazonaws.com:8080/api/lot/lucky/flights?origin=\(model.origin)&numberOfAdults=\(model.numberOfAdults)&numberOfTeenagers=\(model.numberOfTeenagers)&numberOfChildren=\(model.numberOfChildren)&numberOfInfants=\(model.numberOfInfants)"
    }
}
