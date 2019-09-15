//
//  BudgetViewController.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import UIKit

class BudgetViewController: UIViewController {
    
    private let contentService: ContentService = ContentService.shared
    private var maxPrice: Decimal = 0
    
    @IBAction func chooseBudget(_ sender: UIButton) {
        
        switch sender.tag {
        case 1:
            maxPrice = 500
        case 2:
            maxPrice = 1500
        case 3:
            maxPrice = 3000
        case 4:
            maxPrice = 6000
        default:
            break
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        contentService.maxPrice = maxPrice
        let url = UrlHelper.url(for: ContentService.shared)
        
        guard let resultsVC = UIStoryboard.init(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ResultsViewController") as? ResultsViewController else { return }
        resultsVC.url = url
        self.present(resultsVC, animated: true, completion: nil)
    }
}
