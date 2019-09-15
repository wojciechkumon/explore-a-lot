//
//  FinalViewController.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import UIKit

class FinalViewController: UIViewController {
    
    @IBOutlet weak var titleFlight: UILabel!
    @IBOutlet weak var destinationImageView: UIImageView!
    @IBOutlet weak var startLabel: UILabel!
    @IBOutlet weak var finishLabel: UILabel!
    
    var item: WelcomeElement!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.destinationImageView.image = UIImage(named: item.outbound.segments.last!.arrivalAirport)
        self.titleFlight.text = Cities.cities[self.item.outbound.segments.last!.arrivalAirport] ?? ""
        self.startLabel.text = "Start: \(String(self.item.outbound.segments.last!.departureDate.prefix(10)))"
        self.finishLabel.text = "Finish: \(String(self.item.inbound.segments.last!.arrivalDate.prefix(10)))"
    }
    
    @IBAction func bookFlight(_ sender: UIButton) {
        let alert = UIAlertController(title: "Reservation", message: "Your reservation has been finished!", preferredStyle: .alert)
        let action = UIAlertAction(title: "OK", style: .default) { (action) in
            DispatchQueue.main.async {
                guard let finalVC = UIStoryboard.init(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainViewController") as? MainViewController else { return }
                self.present(finalVC, animated: true, completion: nil)
            }
        }

        alert.addAction(action)
        self.present(alert, animated: true, completion: nil)
    }
}
