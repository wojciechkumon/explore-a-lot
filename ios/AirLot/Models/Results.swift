//
//  Results.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import Foundation

// MARK: - WelcomeElement
struct WelcomeElement: Codable {
    let offerID: String
    let totalPrice: TotalPrice
    let outbound, inbound: Bound
    let url: String
    
    enum CodingKeys: String, CodingKey {
        case offerID = "offerId"
        case totalPrice, outbound, inbound, url
    }
}

// MARK: - Bound
struct Bound: Codable {
    let duration: Int
    let segments: [Segment]
    let fareType: String
    let price: Double
    let id: Int
}

// MARK: - Segment
struct Segment: Codable {
    let idInfoSegment: Int
    let departureAirport, arrivalAirport: String
    var departureDate: String
    var arrivalDate: String
    let carrier: String
    let flightNumber: String
    let operationCarrier: String
    let equipment: String
    let duration, stopTime, scheduleChange: Int
}

// MARK: - TotalPrice
struct TotalPrice: Codable {
    let price: Double
    let basePrice: Double
    let tax: Double
    let currency: String
}

typealias Welcome = [WelcomeElement]
