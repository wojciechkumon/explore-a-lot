//
//  LuckyViewController.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//


import UIKit

class LuckyViewController: UIViewController {
    
    @IBOutlet weak var adultLabel: UILabel!
    @IBOutlet weak var teenagerLabel: UILabel!
    @IBOutlet weak var childrenLabel: UILabel!
    @IBOutlet weak var infantsLabel: UILabel!
    
    private var numberOfAdults: Int = 1 {
        didSet {
            DispatchQueue.main.async {
                self.adultLabel.text = "\(self.numberOfAdults) Adults"
            }
        }
    }
    private var numberOfTeenagers: Int = 0 {
        didSet {
            DispatchQueue.main.async {
                self.teenagerLabel.text = "\(self.numberOfTeenagers) Teenagers"
            }
        }
    }
    private var numberOfChildren: Int = 0 {
        didSet {
            DispatchQueue.main.async {
                self.childrenLabel.text = "\(self.numberOfChildren) Children"
            }
        }
    }
    private var numberOfInfants: Int = 0 {
        didSet {
            DispatchQueue.main.async {
                self.infantsLabel.text = "\(self.numberOfInfants) Infants"
            }
        }
    }
    
    private let contentService: ContentService = ContentService.shared
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    @IBAction func plus(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            numberOfAdults += 1
        case 2:
            numberOfTeenagers += 1
        case 4:
            numberOfChildren += 1
        case 6:
            numberOfInfants += 1
        default:
            break
        }
    }
    
    @IBAction func minus(_ sender: UIButton) {
        switch sender.tag {
        case 1:
            numberOfAdults -= 1
        case 3:
            numberOfTeenagers -= 1
        case 5:
            numberOfChildren -= 1
        case 7:
            numberOfInfants -= 1
        default:
            break
        }
    }
    
    @IBAction func onNext(_ sender: UIButton) {
        contentService.numberOfAdults = self.numberOfAdults
        contentService.numberOfTeenagers = self.numberOfTeenagers
        contentService.numberOfChildren = self.numberOfChildren
        contentService.numberOfInfants = self.numberOfInfants
        
        let url = UrlHelper.luckyUrl(for: ContentService.shared)
        
        guard let resultsVC = UIStoryboard.init(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ResultsViewController") as? ResultsViewController else { return }
        resultsVC.url = url
        self.present(resultsVC, animated: true, completion: nil)
    }
}
