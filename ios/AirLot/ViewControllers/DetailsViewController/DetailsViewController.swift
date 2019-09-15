//
//  DetailsViewController.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 14/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import Foundation
import UIKit
import TinderSwipeView

class DetailsViewController: UIViewController {
    
    @IBOutlet weak var swipeView: UIView!
    @IBOutlet weak var welcomePlaceholder: UIStackView!
    @IBOutlet weak var disapproveButton: UIButton!
    @IBOutlet weak var approveButton: UIButton!
    
    private let contentService: ContentService = ContentService.shared
    private var allOptions: [CardModel] = DataService.content()
    private var chosenTags: [Tags] = []
    
    private var swipeViewContainer: TinderSwipeView<CardModel>!{
        didSet{
            self.swipeViewContainer.delegate = self
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Dynamically create view for each tinder card
        let contentView: (Int, CGRect, CardModel) -> (UIView) = { (index: Int ,frame: CGRect , cardModel: CardModel) -> (UIView) in
            let customView = CardView(frame: frame)
            customView.cardModel = cardModel
            return customView
        }
        
        swipeViewContainer = TinderSwipeView<CardModel>(frame: self.swipeView.bounds, contentView: contentView)
        swipeView.addSubview(swipeViewContainer)
        swipeViewContainer.showTinderCards(with: allOptions ,isDummyShow: false)
    }
    
    @IBAction func approve(_ sender: UIButton) {
        if let swipeViewContainer = swipeViewContainer {
            swipeViewContainer.makeRightSwipeAction()
        }
    }
    
    @IBAction func disapprove(_ sender: UIButton) {
        if let swipeViewContainer = swipeViewContainer {
            swipeViewContainer.makeLeftSwipeAction()
        }
    }
    
    
    private func programticViewForOverlay(frame: CGRect, cardModel: CardModel) -> UIView{
        
        let containerView = UIView(frame: frame)
        
        let backGroundImageView = UIImageView(frame:containerView.bounds)
        backGroundImageView.image = UIImage(named:String(Int(1 + arc4random() % (8 - 1))))
        backGroundImageView.contentMode = .scaleAspectFill
        backGroundImageView.clipsToBounds = true
        containerView.addSubview(backGroundImageView)
        
        let profileImageView = UIImageView(frame:CGRect(x: 25, y: frame.size.height - 80, width: 60, height: 60))
        profileImageView.image =  #imageLiteral(resourceName: "circledX")
        profileImageView.contentMode = .scaleAspectFill
        profileImageView.layer.cornerRadius = 30
        profileImageView.clipsToBounds = true
        containerView.addSubview(profileImageView)
        
        let labelText = UILabel(frame:CGRect(x: 90, y: frame.size.height - 80, width: frame.size.width - 100, height: 60))
        labelText.text = "#\(cardModel.tag.rawValue)"
        labelText.numberOfLines = 2
        containerView.addSubview(labelText)
        
        return containerView
    }
    
    @IBAction func start(_ sender: UIButton) {
        UIView.animate(withDuration: 0.3, animations: {
            self.welcomePlaceholder.isHidden = true
        }) { (_) in
            UIView.animate(withDuration: 0.3, animations: {
                self.swipeView.isHidden = false
                self.approveButton.isHidden = false
                self.disapproveButton.isHidden = false
            })
        }
    }
}

extension DetailsViewController: TinderSwipeViewDelegate {
    func dummyAnimationDone() {
        
    }
    
    func currentCardStatus(card: Any, distance: CGFloat) {
        
    }
    
    func fallbackCard(model: Any) {
        
    }
    
    func didSelectCard(model: Any) {
        
    }
    
    func cardGoesLeft(model: Any) {
        
    }
    
    func cardGoesRight(model: Any) {
        let userModel = model as! CardModel
        print("Watchout Left \(userModel.tag)")
        chosenTags.append(userModel.tag)
    }
    
    func undoCardsDone(model: Any) {
        
    }
    
    func endOfCardsReached() {
        self.contentService.tags = chosenTags
        UIView.animate(withDuration: 2.0, delay: 0.0, options: .curveLinear, animations: {
            self.approveButton.isHidden = true
            self.disapproveButton.isHidden = true
        }, completion: { _ in
            if let dateVC = UIStoryboard.init(name: "Main", bundle: nil).instantiateViewController(withIdentifier: DateViewController.className) as? DateViewController {
                self.present(dateVC, animated: true, completion: nil)
            }
        })
        print("End of all cards")
    }
}
