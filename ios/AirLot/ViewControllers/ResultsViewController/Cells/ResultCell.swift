//
//  ResultCell.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import UIKit


class ResultCell: UITableViewCell {
    
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var destinationLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var backgroundImage: UIImageView!
    
    var model: WelcomeElement! {
        didSet {
            let startDate: String = String(self.model.outbound.segments.last!.departureDate.prefix(10))
            let finishDate: String = String(self.model.inbound.segments.last!.arrivalDate.prefix(10))
            self.dateLabel.text = "\(startDate.description)-\(finishDate.description)"
            self.destinationLabel.text = Cities.cities[self.model.outbound.segments.last!.arrivalAirport] ?? ""
            self.priceLabel.text = "\(self.model.totalPrice.price) PLN return / person"
            self.backgroundImage.image = UIImage(named: self.model.outbound.segments.last!.arrivalAirport)
            self.backgroundImage.autoresizingMask = [.flexibleWidth, .flexibleHeight]
            self.backgroundImage.translatesAutoresizingMaskIntoConstraints = true
            self.backgroundImage.contentMode = .scaleAspectFill
        }
    }
}
